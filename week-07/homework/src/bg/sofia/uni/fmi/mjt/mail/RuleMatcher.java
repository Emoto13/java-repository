package bg.sofia.uni.fmi.mjt.mail;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class RuleMatcher {

    public static String matchRules(Mail mail, Collection<Rule> rawRules) {
        List<Rule> rules = new ArrayList<Rule>(rawRules);
        rules.sort(new RuleComparator());
        for (Rule rule : rules) {
            if (rule.matches(mail)) {
                return rule.getPath();
            }
        }
        return MailFolder.INBOX;
    }

    public static boolean verifyRuleUniqueness(Rule rule, Collection<Rule> rawRules) {
        List<Rule> rules = new ArrayList<Rule>(rawRules);
        rules.sort(new RuleComparator());

        for (Rule toCompare : rules) {
            if (toCompare.equals(rule)) {
                return false;
            }
        }
        return true;
    }
}
