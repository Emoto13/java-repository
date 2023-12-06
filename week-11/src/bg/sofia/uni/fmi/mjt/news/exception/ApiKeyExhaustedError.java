package bg.sofia.uni.fmi.mjt.news.exception;

public class ApiKeyExhaustedError extends ApiError {
    public ApiKeyExhaustedError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
