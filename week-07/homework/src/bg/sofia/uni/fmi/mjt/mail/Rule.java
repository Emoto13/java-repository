package bg.sofia.uni.fmi.mjt.mail;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    public static final int MIN_PRIORITY = 0;
    public static final int MAX_PRIORITY = 10;

    private Set<String> subjectKeywords;
    private Set<String> bodyKeywords;
    private Set<String> recipients;
    private String sender;
    private String path;
    private int priority;
    private String definition;

    public Rule(Set<String> subjectKeywords, Set<String> bodyKeywords, Set<String> recipients,
               String sender, String path, int priority, String definition) {
        this.subjectKeywords = subjectKeywords;
        this.bodyKeywords = bodyKeywords;
        this.recipients = recipients;
        this.sender = sender;
        this.path = path;
        this.setPriority(priority);
        this.definition = definition;
    }

    private static Set<String> getKeywords(String definition, String pattern, String separator) {
        Set<String> result = new HashSet<String>();
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(definition);
        if (!m.find()) {
            return result;
        }

        String rawKeywords = m.group(1);
        String[] keywords = rawKeywords.split(separator);
        Collections.addAll(result, keywords);
        return result;
    }

    private static String getSender(String definition) {
        Pattern r = Pattern.compile("from: (( ?.*)*)");
        Matcher m = r.matcher(definition);
        if (!m.find()) {
            return "";
        }

        return m.group(1);
    }
      
    public static Rule createRule(String definition, String path, int priority) {
        Set<String> subjectKeywords = Rule.getKeywords(definition, "subject-includes: (([ ]?.*,?)*)", ",[ ]?");
        Set<String> bodyKeywords = Rule.getKeywords(definition, "subject-or-body-includes: (([ ]?.*,?)*)", ",[ ]?");
        subjectKeywords.addAll(bodyKeywords);

        Set<String> recipients = Rule.getKeywords(definition, "recipients-includes: (([ ]?.*,?)*)", ",[ ]?");
        String sender = Rule.getSender(definition);
        return new Rule(subjectKeywords, bodyKeywords, recipients, sender, path, priority, definition);
    }

    public void setPriority(int priority) {
        if (priority < Rule.MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new IllegalArgumentException("Priority should be in range [0, 10]");
        }
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getPath() {
        return path;
    }

    public String getDefinition() {
        return definition;
    }

    public boolean matches(Mail mail) {
        for (String keyword : subjectKeywords) {
            if (mail.subject().contains(keyword)) {
                return true;
            }
        }

        for (String keyword : bodyKeywords) {
            if (mail.body().contains(keyword)) {
                return true;
            }
        }

        for (String recipient : recipients) {
            if (mail.recipients().contains(recipient)) {
                return true;
            }
        }

        return mail.sender().name().equals(sender);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Rule other)) {
            return false;
        }

        return this.priority == other.getPriority() &&
                this.definition.equals(other.getDefinition()) &&
                !this.path.equals(other.getPath());
    }
}

