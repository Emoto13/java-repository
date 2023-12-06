package bg.sofia.uni.fmi.mjt.mail;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;

public class Outlook implements MailClient {
    private Map<String, Account> accounts;
    private Map<String, MailFolder> accountNameToFolder;
    private Map<String, String> mailToAccountName;


    private Map<String, List<Rule>> accountNameToRules;

    public Outlook() {
        this.accounts = new HashMap<String, Account>();
        this.accountNameToFolder = new HashMap<String, MailFolder>();
        this.mailToAccountName = new HashMap<String, String>();
        this.accountNameToRules = new HashMap<String, List<Rule>>();
    }

    private void createNewAccount(Account account) {
        accounts.put(account.name(), account);
        accountNameToRules.put(account.name(), new ArrayList<>());
        MailFolder folder = new MailFolder(true);
        accountNameToFolder.put(account.name(), folder);
        mailToAccountName.put(account.emailAddress(), account.name());
    }

    private Mail toSentMail(Account account, String metadata, String content) {
        Set<String> recipients = MailConverter.rawToSetField(metadata, "recipients: (([ ]?.*,?)*)", ",[ ]?");
        String subject = MailConverter.rawToStringField(metadata, "subject: (([ ]?.*)*)");
        String received = MailConverter.rawToStringField(metadata, "received: (([ ]?.*)*)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return new Mail(account, recipients, subject,  content, LocalDateTime.parse(received, formatter));
    }

    private Mail toReceivedMail(String metadata, String content) {
        Set<String> recipients = MailConverter.rawToSetField(metadata, "recipients: (([ ]?.*,?)*)", ",[ ]?");
        String subject = MailConverter.rawToStringField(metadata, "subject: (([ ]?.*)*)");
        String received = MailConverter.rawToStringField(metadata, "received: (([ ]?.*)*)");
        String senderMail = MailConverter.rawToStringField(metadata, "sender: (( ?.*)*)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (!mailToAccountName.containsKey(senderMail)) {
            throw new AccountNotFoundException("No such sender found");
        }

        return new Mail(accounts.get(mailToAccountName.get(senderMail)), recipients, subject,  content,
                LocalDateTime.parse(received, formatter));
    }


    private void addRule(String accountName, Rule rule) {
        this.accountNameToFolder.get(accountName).getParentFolder(rule.getPath()); // checks if such path exists
        List<Rule> rules = this.accountNameToRules.get(accountName);
        if (!RuleMatcher.verifyRuleUniqueness(rule, rules)) {
            throw new RuleAlreadyDefinedException("Rule is already defined");
        }
        
        rules.add(rule);
        this.accountNameToRules.put(accountName, rules);
        this.accountNameToFolder.get(accountName).applyRule(rule, MailFolder.INBOX);
    }

    @Override
    public Account addNewAccount(String accountName, String email) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("Invalid account name");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Invalid email");
        if (accounts.containsKey(accountName)) {
            throw new AccountAlreadyExistsException("Account already exists");
        }
        if (mailToAccountName.containsKey(email)) {
            throw new AccountAlreadyExistsException("Account already exists");
        }

        Account account = new Account(email, accountName);
        this.createNewAccount(account);
        return account;
    }

    @Override
    public void createFolder(String accountName, String path) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (path == null || path.isBlank()) throw new IllegalArgumentException("invalid path");

        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account does not exists");
        }

        if (path.equals(MailFolder.ROOT)) throw new FolderAlreadyExistsException("Root path already exists");

        MailFolder folder = accountNameToFolder.get(accountName);
        folder.insertFolder(path);
    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (folderPath == null || folderPath.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (ruleDefinition == null || ruleDefinition.isBlank()) {
            throw new IllegalArgumentException("invalid account name");
        }
        if (priority < Rule.MIN_PRIORITY || priority > Rule.MAX_PRIORITY)  {
            throw new IllegalArgumentException("Priority should be in range [0, 10]");
        }
        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account does not exists");
        }

        Rule rule = Rule.createRule(ruleDefinition, folderPath, priority);
        addRule(accountName, rule);        
    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (mailMetadata == null || mailMetadata.isBlank()) throw new IllegalArgumentException("invalid mail metadata");
        if (mailContent == null || mailContent.isBlank()) throw new IllegalArgumentException("invalid mail content");
        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account does not exists");
        }

        List<Rule> rules = accountNameToRules.get(accountName);
        Mail mail = toReceivedMail(mailMetadata, mailContent);
        String path = RuleMatcher.matchRules(mail, rules);

        accountNameToFolder.get(accountName).insertEmail(path, mail);
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String accountName, String folderPath) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (folderPath == null || folderPath.isBlank()) throw new IllegalArgumentException("invalid path");
        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account does not exists");
        }

        return accountNameToFolder.get(accountName).getMails(folderPath);
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        if (accountName == null || accountName.isBlank()) throw new IllegalArgumentException("invalid account name");
        if (mailMetadata == null || mailMetadata.isBlank()) throw new IllegalArgumentException("invalid mail metadata");
        if (mailContent == null || mailContent.isBlank()) throw new IllegalArgumentException("invalid mail content");
        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account does not exists");
        }

        Account account = accounts.get(accountName);
        mailMetadata = MailConverter.replaceSender(mailMetadata, account.emailAddress());
        Mail sentMail = toSentMail(account, mailMetadata, mailContent);
        MailFolder folder = accountNameToFolder.get(accountName);
        folder.insertEmail(MailFolder.SENT, sentMail);

        for (String recipientMail : sentMail.recipients()) {
            if (!mailToAccountName.containsKey(recipientMail)) {
                continue;
            }

            receiveMail(mailToAccountName.get(recipientMail), mailMetadata, mailContent);
        }
    }
}
