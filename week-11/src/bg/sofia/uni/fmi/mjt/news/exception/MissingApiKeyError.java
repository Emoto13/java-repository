package bg.sofia.uni.fmi.mjt.news.exception;

public class MissingApiKeyError extends ApiError {
    public MissingApiKeyError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
