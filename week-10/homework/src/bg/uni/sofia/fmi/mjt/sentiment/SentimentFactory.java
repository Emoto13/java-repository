package bg.uni.sofia.fmi.mjt.sentiment;

public class SentimentFactory {
    public static Sentiment createSentiment(int value) {
        if (value == Sentiment.NEGATIVE.getIntValue()) {
            return Sentiment.NEGATIVE;
        } else if (value == Sentiment.SOMEWHAT_NEGATIVE.getIntValue()) {
            return Sentiment.SOMEWHAT_NEGATIVE;
        } else if (value == Sentiment.NEUTRAL.getIntValue()) {
            return Sentiment.NEUTRAL;
        } else if (value == Sentiment.SOMEWHAT_POSITIVE.getIntValue()) {
            return Sentiment.SOMEWHAT_POSITIVE;
        } else if (value == Sentiment.POSITIVE.getIntValue()) {
            return Sentiment.POSITIVE;
        }
        return Sentiment.UNKNOWN;
    }

}
