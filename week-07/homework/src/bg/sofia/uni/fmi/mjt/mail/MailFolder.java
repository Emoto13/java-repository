package bg.sofia.uni.fmi.mjt.mail;

import java.util.*;

import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;

public class MailFolder {
    public static final String ROOT = "/";
    public static final String INBOX = "/inbox";
    public static final String SENT = "/sent";

    private Map<String, MailFolder> folders;
    private Set<Mail> mails;

    public MailFolder() {
        this.folders = new HashMap<String, MailFolder>();
        this.mails = new HashSet<Mail>();
    }

    public MailFolder(boolean isRoot) {
        this.folders = new HashMap<String, MailFolder>();
        this.mails = new HashSet<Mail>();
        if (isRoot) this.createInitialStructure();
    }

    private void createInitialStructure() {
        MailFolder inbox = this.addFolder("inbox");
        MailFolder important = inbox.addFolder("important");
        important.addFolder("personal");
        important.addFolder("work");
        this.addFolder("sent");
    }

    public <K extends Number> boolean areEqual(K p1, K p2) {
        return true;
    }

    private String cleanPath(String path) {
        path = path.trim();
        if (path.length() > 1 && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public MailFolder getParentFolder(String path) {
        if (!path.startsWith(MailFolder.ROOT)) {
            throw new InvalidPathException("Should start path with /");
        }

        if (path.equals(MailFolder.ROOT)) {
            return this;
        }

        path = cleanPath(path);
        String[] absolutePath = path.substring(1).split(MailFolder.ROOT);

        MailFolder structure = this;
        for (String subPath : absolutePath) {
            if (!structure.hasPath(subPath)) {
                throw new FolderNotFoundException("No such folder");
            }
            structure = structure.getSubfolders(subPath);
        }
        return structure;
    }

    public void addMail(Mail mail) {
        mails.add(mail);
    }

    public MailFolder addFolder(String name) {
        if (folders.containsKey(name)) {
            throw new FolderAlreadyExistsException("Folder already exists");
        }

        MailFolder folder = new MailFolder();
        folders.put(name, folder);
        return folder;
    }

    public void insertEmail(String path, Mail mail) {
        path = cleanPath(path);
        MailFolder structure = getParentFolder(path);
        structure.addMail(mail);
    }

    public void insertFolder(String path) {
        path = cleanPath(path);
        String[] absolutePath = path.substring(1).split(MailFolder.ROOT);
        String pathToUse = path.substring(0, path.lastIndexOf("/"));
        if (pathToUse.isBlank()) pathToUse = MailFolder.ROOT;

        MailFolder structure = getParentFolder(pathToUse);
        structure.addFolder(absolutePath[absolutePath.length - 1]);
    }

    public Collection<Mail> getMails(String path) {
        path = cleanPath(path);
        if (!path.startsWith(MailFolder.ROOT)) {
            throw new InvalidPathException("Should start path with /");
        }
    
        MailFolder structure = getParentFolder(path);
        return structure.getMails();
    }

    public boolean hasPath(String path) {
        path = cleanPath(path);
        return folders.containsKey(path);
    }

    public MailFolder getSubfolders(String path) {
        path = cleanPath(path);
        return folders.get(path);
    }

    public Collection<Mail> getMails() {
        return mails;
    }

    public void applyRule(Rule rule, String path) {
        path = cleanPath(path);
        MailFolder folder = this.getParentFolder(path);
        Set<Mail> matchedMails = new HashSet<>();
        for (Mail mail : folder.getMails()) {
            if (rule.matches(mail)) matchedMails.add(mail);
        }

        MailFolder newFolder = this.getParentFolder(rule.getPath());
        for (Mail mail : matchedMails) {
            newFolder.addMail(mail);
            folder.mails.remove(mail);
        }
    }
}
