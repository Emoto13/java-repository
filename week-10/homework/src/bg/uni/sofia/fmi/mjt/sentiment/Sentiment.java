package bg.uni.sofia.fmi.mjt.sentiment;

public enum Sentiment {
    NEGATIVE("negative", 0.0),
    SOMEWHAT_NEGATIVE("somewhat negative", 1.0),
    NEUTRAL("neutral", 2.0),
    SOMEWHAT_POSITIVE("somewhat positive", 3.0),
    POSITIVE("positive", 4.0),
    UNKNOWN("UNKNOWN", -1.0);

    public static final int LOW_BOUNDARY = 0;
    public static final int HIGH_BOUNDARY = 4;

    private final String name;
    private final double value;

    Sentiment(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getIntValue() {
        return (int) Math.round(value);
    }
}
