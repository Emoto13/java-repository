package vault;

import exception.FailedToEncryptPassword;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PasswordEncryptor {
    private static final String MD5 = "MD5";
    private static PasswordEncryptor instance = null;
    private final MessageDigest digest;

    private PasswordEncryptor() throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance(MD5);
    }

    public String encrypt(String password) {
        byte[] message = password.getBytes(StandardCharsets.UTF_8);
        return Arrays.toString(digest.digest(message));
    }


    public static PasswordEncryptor getInstance() throws FailedToEncryptPassword {
        if (instance != null) {
            return instance;
        }

        try {
            instance = new PasswordEncryptor();
        } catch (NoSuchAlgorithmException e) {
            throw new FailedToEncryptPassword("Failed to create encryptor");
        }
        return instance;
    }
}
