package vault;

import entity.CreateUserRequest;
import exception.FailedToCreateUser;

import java.io.IOException;
import java.io.Writer;

public class PasswordVault {
    private final Writer passwordWriter;

    public PasswordVault(Writer passwordWriter) {
        this.passwordWriter = passwordWriter;
    }

    public void createUser(CreateUserRequest request) throws FailedToCreateUser {
        try {
            this.passwordWriter.write(request.toString());
        } catch (IOException e) {
            throw new FailedToCreateUser("Failed to create user");
        }
    }
}
