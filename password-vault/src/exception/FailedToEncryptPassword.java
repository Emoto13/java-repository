package exception;

public class FailedToEncryptPassword extends Exception {

    public FailedToEncryptPassword(String message) {
        super(message);
    }
}
