package vault;

import entity.RegisterUserRequest;
import exception.FailedToCreateUser;
import exception.FailedToEncryptPassword;

public class PasswordVaultHandler {
    private final PasswordVaultController controller;

    public PasswordVaultHandler(PasswordVaultController controller) {
        this.controller = controller;
    }

    public void registerUser(String user, String password, String repeatedPassword) throws FailedToEncryptPassword, FailedToCreateUser {
        RegisterUserRequest request = new RegisterUserRequest(user, password, repeatedPassword);
        controller.registerUser(request);
    }
}
