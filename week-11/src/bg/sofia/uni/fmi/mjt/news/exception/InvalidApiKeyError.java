package bg.sofia.uni.fmi.mjt.news.exception;

public class InvalidApiKeyError extends ApiError {
    public InvalidApiKeyError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}