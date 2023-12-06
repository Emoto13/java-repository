package bg.sofia.uni.fmi.mjt.markdown;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MarkdownConverterTest {
    @Test
    void test() {
        MarkdownConverterAPI markdownConverter = new MarkdownConverter();
        File f = new File("README.md");
        File f2 = new File("some.txt");

        assertDoesNotThrow(() -> {
            f.createNewFile();
            f2.createNewFile();
        });


        markdownConverter.convertMarkdown(new StringReader("abv"), new StringWriter());
        markdownConverter.convertMarkdown(Paths.get("./README.md"), Paths.get("./res.html"));
        markdownConverter.convertMarkdown(Paths.get(""), null);
        markdownConverter.convertMarkdown(new StringReader("abv"), null);
        Path sourceDir = Paths.get(".");
        markdownConverter.convertAllMarkdownFiles(sourceDir, sourceDir);
    }
}