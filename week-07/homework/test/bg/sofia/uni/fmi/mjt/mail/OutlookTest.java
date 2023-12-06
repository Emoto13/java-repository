package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutlookTest {
    @Test
    void TestAddNewAccount() {
        MailClient outlook = new Outlook();
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addNewAccount("", "email@email.com");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addNewAccount(null, "email@email.com");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addNewAccount("name", "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addNewAccount("name", null);
        });

        Account accountAdded = outlook.addNewAccount("name", "email@email.com");
        assertEquals(accountAdded.name(), "name");
        assertEquals(accountAdded.emailAddress(), "email@email.com");

        assertDoesNotThrow(() -> {
            outlook.getMailsFromFolder("name", "/");
        });

        assertThrows(AccountAlreadyExistsException.class, () -> {
            outlook.addNewAccount("name", "email2@email.com");
        });

        assertThrows(AccountAlreadyExistsException.class, () -> {
            outlook.addNewAccount("name2", "email@email.com");
        });

    }

    @Test
    void TestCreateFolder() {
        MailClient outlook = new Outlook();
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.createFolder("", "/path");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.createFolder(null, "/path");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.createFolder("name", "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.createFolder("name", null);
        });
        assertThrows(AccountNotFoundException.class, () -> {
            outlook.createFolder("name", "/path");
        });
        outlook.addNewAccount("name", "email@email.com");
        assertThrows(FolderAlreadyExistsException.class, () -> {
            outlook.createFolder("name", MailFolder.ROOT);
        });
        assertThrows(FolderAlreadyExistsException.class, () -> {
            outlook.createFolder("name", MailFolder.INBOX);
        });

        outlook.createFolder("name", "/inbox/folder/");
        assertDoesNotThrow(() -> {
            outlook.getMailsFromFolder("name", "/inbox/folder");
        });

        outlook.createFolder("name", "/folder");
        assertDoesNotThrow(() -> {
            outlook.getMailsFromFolder("name", "/folder");
        });
    }

    @Test
    void TestAddRule() {
        MailClient outlook = new Outlook();

        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addRule("", "/path", "a", 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addRule("name", "", "a", 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addRule("name", "/path", "", 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addRule("name", "/path", "a", -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.addRule("name", "/path", "a", 11);
        });
        assertThrows(AccountNotFoundException.class, () -> {
            outlook.addRule("name", "/path", "a", 1);
        });

        outlook.addNewAccount("name", "email@email.com");
        assertThrows(FolderNotFoundException.class, () -> {
            outlook.addRule("name", "/non-existent", "a", 1);
        });

        assertThrows(RuleAlreadyDefinedException.class, () -> {
            outlook.addRule("name", "/inbox/important", "a", 1);
            outlook.addRule("name", "/inbox", "a", 1);
        });


    }

    @Test
    void TestSendMail() {
        MailClient outlook = new Outlook();
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.sendMail("", "meta", "content");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.sendMail("name", "", "content");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.sendMail("name", "meta", "");
        });
        assertThrows(AccountNotFoundException.class, () -> {
            outlook.sendMail("name", "meta", "content");
        });

        outlook.addNewAccount("name", "email@email.com");
        outlook.addNewAccount("name2", "testy@gmail.com");
        outlook.sendMail("name2", """
                     * sender: testy@gmail.com
                     * subject: Hello, MJT!
                     * recipients: email@email.com
                     * received: 2022-12-08 14:14
                """, "some content");
        outlook.sendMail("name2", """
                     * sender: testy@gmail.com
                     * subject: Hello, Some!
                     * recipients: email@email.com, notexisting@fake.com
                     * received: 2022-12-08 14:14
                """, "some content");

        outlook.addRule("name", "/inbox/important/work", """
                subject-or-body-includes: MJT
                """, 1);


        assertEquals(1, outlook.getMailsFromFolder("name", "/inbox").size());
        assertEquals(1, outlook.getMailsFromFolder("name", "/inbox/important/work").size());
        assertEquals(2, outlook.getMailsFromFolder("name2", "/sent").size());
    }

    @Test
    void TestGetMailsFromFolder() {
        MailClient outlook = new Outlook();
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.getMailsFromFolder("", "/path");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.getMailsFromFolder("name", "");
        });
        assertThrows(AccountNotFoundException.class, () -> {
            outlook.getMailsFromFolder("name", "/path");
        });
    }

    @Test
    void TestReceiveMail() {
        MailClient outlook = new Outlook();
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.receiveMail("", "meta", "content");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.receiveMail("name", "", "content");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            outlook.receiveMail("name", "meta", "");
        });
        assertThrows(AccountNotFoundException.class, () -> {
            outlook.receiveMail("name", "meta", "content");
        });

        outlook.addNewAccount("name", "email@email.com");
        outlook.addNewAccount("name2", "testy@gmail.com");
        outlook.receiveMail("name2", """
                     * sender: testy@gmail.com
                     * subject: Hello, MJT!
                     * recipients: email@email.com
                     * received: 2022-12-08 14:14
                """, "some content");

        assertThrows(AccountNotFoundException.class, () -> {
                    outlook.receiveMail("name2", """
                                 * sender: doesntexist@gmail.com
                            """, "some content");
                });

        assertEquals(1, outlook.getMailsFromFolder("name2", "/inbox").size());
    }
}