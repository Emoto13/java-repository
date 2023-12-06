package bg.uni.sofia.fmi.mjt.sentiment;

import java.util.Arrays;

public class Review {
    private final double sentiment;
    private final String[] words;

    public Review(double sentiment, String[] words) {
        this.sentiment = sentiment;
        this.words = words;
    }

    public Review(double sentiment, String rawReview) {
        this.sentiment = sentiment;
        this.words = reviewToWords(rawReview);
    }

    public static String[] reviewToWords(String rawReview) {
        rawReview = rawReview.toLowerCase();
        rawReview = rawReview.replaceAll("[^a-zA-Z0-9']", " ");
        rawReview = rawReview.trim();
        return rawReview.split("\\s+");
    }

    public static Review toReview(String rawReview) {
        String[] tokens = reviewToWords(rawReview);
        double sentiment = Double.parseDouble(tokens[0]);
        String[] reviewTokens = Arrays.stream(tokens, 1, tokens.length)
                .toArray(String[]::new);

        return new Review(sentiment, reviewTokens);
    }

    public double getSentiment() {
        return sentiment;
    }

    public String[] getWords() {
        return words;
    }
}
