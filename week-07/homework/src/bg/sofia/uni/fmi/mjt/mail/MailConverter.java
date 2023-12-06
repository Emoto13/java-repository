package bg.sofia.uni.fmi.mjt.mail;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailConverter {
    public static Set<String> rawToSetField(String definition, String pattern, String separator) {
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

    public static String rawToStringField(String definition, String pattern) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(definition);
        if (!m.find()) {
            return "";
        }
  
        return m.group(1);
    }

    public static String replaceSender(String metadata, String correctSenderMail) {
        if (!metadata.contains("sender:")) {
            return metadata + String.format("\nsender: %s\n", correctSenderMail);
        }

        String senderMail = MailConverter.rawToStringField(metadata, "sender: (( ?.*)*)");
        if (!senderMail.equals(correctSenderMail)) {
            return metadata.replaceAll("sender: (( ?.*)*)", String.format("sender: %s", correctSenderMail));
        }
        return metadata;
    }
}
