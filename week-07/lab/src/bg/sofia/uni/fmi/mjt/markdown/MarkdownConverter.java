package bg.sofia.uni.fmi.mjt.markdown;

import java.io.Reader;
import java.io.Writer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownConverter implements MarkdownConverterAPI {
    private String replaceHeaders(String text) {
        text = replace(text, "###### (.*)", "<h6>", "</h6>");
        text = replace(text, "##### (.*)", "<h5>", "</h5>");
        text = replace(text, "#### (.*)", "<h4>", "</h4>");
        text = replace(text, "### (.*)", "<h3>", "</h3>");
        text = replace(text, "## (.*)", "<h2>", "</h2>");
        text = replace(text, "# (.*)", "<h1>", "</h1>");
        return text;
    }

    private String replaceStyles(String text) {
        text = replace(text, "\\*\\*(.*)\\*\\*", "<strong>", "</strong>");
        text = replace(text, "\\*(.*)\\*", "<em>", "</em>");
        return text;
    }

    private String replaceCode(String text) {
        text = replace(text, "`(.*)`", "<code>", "</code>");
        return text;
    }

    private String replace(String text, String pattern, String startTag, String endTag) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        while (m.find()) {
            String rawText = m.group(1);
            text = text.replaceFirst(pattern, String.format("%s%s%s", startTag, rawText, endTag));
            m = r.matcher(text);
        }
        return text;
    }

    private String toMarkdown(String text) {
        text = replaceHeaders(text);
        text = replaceStyles(text);
        text = replaceCode(text);
        text = String.format("%s%s%s", "<html>\n<body>\n", text, "</body>\n</html>\n");
        return text;
    }

    @Override
    public void convertMarkdown(Reader source, Writer output) {
        StringBuilder sb = new StringBuilder();

        try (source) {
            int value;
            while ((value = source.read()) != -1) {
                sb.append((char) value);
            }

            String markdown = toMarkdown(sb.toString());
            output.write(markdown);
            output.flush();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void convertMarkdown(Path from, Path to) {
        try {
            FileReader reader = new FileReader(from.toString());
            FileWriter writer = new FileWriter(to.toString());    
            convertMarkdown(reader, writer);
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        
    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {
        File folder = new File(sourceDir.toString());
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file == null || !file.isFile()) {
                continue;
            }

            if (file.getName().endsWith(".md")) {
                String name = file.getName().replace(".md", ".html");
                String fullName = String.format("%s/%s", targetDir.toString(), name);

                System.out.printf("%s | %s  | %s\n", sourceDir.toString(), targetDir.toString(), fullName);

                Path output = Paths.get(fullName);
                convertMarkdown(file.toPath(), output);
            }        
        }
    }
}