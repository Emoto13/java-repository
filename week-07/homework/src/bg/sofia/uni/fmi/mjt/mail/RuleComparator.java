package bg.sofia.uni.fmi.mjt.mail;

import java.util.Comparator;

public class RuleComparator implements Comparator<Rule> {
    @Override
    public int compare(Rule firstRule, Rule secondRule) {
        return secondRule.getPriority() - firstRule.getPriority();
    }
}
