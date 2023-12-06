package entity;

public record CreateUserRequest(String user, String password) {

    @Override
    public String toString() {
        return String.format("%s,%s", user, password);
    }
}
