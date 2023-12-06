package bg.sofia.uni.fmi.mjt.news.exception;

public class NotExistingSourceError extends ApiError {
    public NotExistingSourceError(String message) {
        super(message);
    }

    @Override
    public boolean isCritical() {
        return true;
    }
}
