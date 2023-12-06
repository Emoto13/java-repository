package vault;

import entity.CreateUserRequest;
import entity.RegisterUserRequest;
import exception.FailedToCreateUser;
import exception.FailedToEncryptPassword;

public class PasswordVaultController {
    private PasswordVault vault;

    public PasswordVaultController(PasswordVault vault) {
        this.vault = vault;
    }

    public void registerUser(RegisterUserRequest request) throws FailedToEncryptPassword, FailedToCreateUser {
        PasswordEncryptor encryptor = PasswordEncryptor.getInstance();
        String encryptedPassword = encryptor.encrypt(request.password());
        CreateUserRequest createUserRequest = new CreateUserRequest(request.user(), encryptedPassword);
        this.vault.createUser(createUserRequest);
    }

    public void loginUser() {

    }

    public void retrieveCredentials() {

    }

    public void generatePassword() {

    }

    public void addPassword() {

    }

    public void removePassword() {

    }

    public void disconnect() {

    }
}
