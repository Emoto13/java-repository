package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MailFolderTest {
    @Test
    void TestGetParentFolder() {
        MailFolder folder = new MailFolder();
        assertThrows(InvalidPathException.class, () -> {
            folder.getParentFolder("invalid/path");
        });
    }

    @Test
    void TestGetMails() {
        MailFolder folder = new MailFolder();
        assertThrows(InvalidPathException.class, () -> {
            folder.getMails("invalid/path");
        });
    }
}
