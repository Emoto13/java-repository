package entity;

import verification.Verifier;

public record RegisterUserRequest(String user, String password, String repeatedPassword) {
    public RegisterUserRequest {
        Verifier.assertNotEmptyOrNull(user, "User should not be empty or null");
        Verifier.assertNotEmptyOrNull(password, "Password should not be empty or null");
        Verifier.assertNotEmptyOrNull(repeatedPassword, "Repeated password should not be empty or null");
        Verifier.assertEqual(password, repeatedPassword, "Passwords should match");
    }
}
