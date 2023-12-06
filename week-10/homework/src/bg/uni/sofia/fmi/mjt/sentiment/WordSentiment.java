package bg.uni.sofia.fmi.mjt.sentiment;

public class WordSentiment {
    private final String word;
    private double totalSentiment;
    private int frequency;

    public WordSentiment(String word, double totalSentiment, int frequency) {
        this.word = word;
        this.totalSentiment = totalSentiment;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public double getSentiment() {
        return totalSentiment / frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void addSentiment(double sentiment) {
        this.totalSentiment += sentiment;
        this.frequency++;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s -> Sentiment: %.2f -> Frequency %d", word, getSentiment(), getFrequency());
    }

}