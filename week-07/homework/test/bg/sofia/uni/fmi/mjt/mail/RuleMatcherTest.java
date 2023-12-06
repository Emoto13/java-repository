package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleMatcherTest {
    @Test
    void TestMatchRules() {
        Mail mail = new Mail(new Account("email.com","name"),
                new HashSet<>(), "", "", LocalDateTime.now());
        Rule rule1 = new Rule(new HashSet<>(), new HashSet<>(), new HashSet<>(), "name", "/correct-path", 2, "");
        Rule rule2 = new Rule(new HashSet<>(), new HashSet<>(), new HashSet<>(), "name", "/incorrect-path", 1, "");

        String path = RuleMatcher.matchRules(mail, Arrays.asList(rule1, rule2));
        assertEquals("/correct-path",path);
    }
}
