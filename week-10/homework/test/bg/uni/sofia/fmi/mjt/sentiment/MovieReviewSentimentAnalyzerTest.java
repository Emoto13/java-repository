package bg.uni.sofia.fmi.mjt.sentiment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovieReviewSentimentAnalyzerTest {
    private SentimentAnalyzer analyzer;
    private Writer reviewWriter;
    private Reader stopWordReader;
    private Reader reviewReader;

    @BeforeEach
    void setupTest() {
        this.stopWordReader = new StringReader("""
                the
                and
                is
                """);
        this.reviewReader = new StringReader("""
                0 negative
                1 some--review here
                2 there is another one here
                3 and another one here and here
                4 the last review
                """);
        this.reviewWriter = new StringWriter();
        this.analyzer = new MovieReviewSentimentAnalyzer(stopWordReader, reviewReader, reviewWriter);
    }

    @Test
    void getReviewSentiment_WorksCorrectly() {
        String rawReview = "Weird sentiment Review . here";
        double sentiment = analyzer.getReviewSentiment(rawReview);
        assertEquals(2.25, sentiment, "sentiment should be calculated correctly");
    }

    @Test
    void getReviewSentiment_WorksCorrectly_ForUnknown() {
        String rawReview = "Weird sentiment unknown";
        double sentiment = analyzer.getReviewSentiment(rawReview);
        assertEquals(-1, sentiment, "unknown sentiment should be calculated correctly");
    }

    @Test
    void getReviewSentimentAsName_WorksCorrectly() {
        Object[][] testCases = new Object[][]{
                {"negative", Sentiment.NEGATIVE.getName()},
                {"some", Sentiment.SOMEWHAT_NEGATIVE.getName()},
                {"there", Sentiment.NEUTRAL.getName()},
                {"another", Sentiment.SOMEWHAT_POSITIVE.getName()},
                {"last", Sentiment.POSITIVE.getName()},
                {"unknown", Sentiment.UNKNOWN.getName()}
        };

        for (Object[] testCase : testCases) {
            String name = analyzer.getReviewSentimentAsName((String) testCase[0]);
            String expected = (String) testCase[1];
            assertEquals(expected, name, "name should match expected sentiment name");
        }
    }

    @Test
    void getWordSentiment_WorksCorrectly() {
        Object[][] testCases = new Object[][]{
                {"NEgative", Sentiment.NEGATIVE.getValue(), "existing word"},
                {"unknown", Sentiment.UNKNOWN.getValue(), "non existing word"}
        };

        for (Object[] testCase : testCases) {
            double value = analyzer.getWordSentiment((String) testCase[0]);
            double expected = (double) testCase[1];
            assertEquals(expected, value, "should calculate word sentiment correctly");
        }
    }

    @Test
    void getWordFrequency_WorksCorrectly() {
        Object[][] testCases = new Object[][]{
                {"negative", 1},
                {"here", 3},
                {"unknown", 0}
        };

        for (Object[] testCase : testCases) {
            int value = analyzer.getWordFrequency((String) testCase[0]);
            int expected = (int) testCase[1];
            assertEquals(expected, value, "should calculate word frequency correctly");
        }
    }

    @Test
    void getMostFrequentWords_WorksCorrectly() {
        List<Integer> input = Arrays.asList(1, 4, -1);
        List<List<String>> expected = Arrays.asList(List.of("here"), Arrays.asList("here", "one", "review", "another"), null);
        List<Boolean> shouldThrowException = Arrays.asList(false, false, true);
        List<Exception> exceptions = Arrays.asList(null, null, new IllegalArgumentException());

        for (int i = 0; i < input.size(); i++) {
            int inputValue = input.get(i);
            if (shouldThrowException.get(i)) {
                assertThrows(exceptions.get(i).getClass(), () -> {
                    analyzer.getMostFrequentWords(inputValue);
                });
            } else {
                List<String> value = analyzer.getMostFrequentWords(inputValue);
                List<String> expectedValue = expected.get(i);
                assertEquals(Set.copyOf(expectedValue), Set.copyOf(value), "should calculate most frequent words correctly");
            }
        }
    }

    @Test
    void getMostPositiveWords_WorkCorrectly() {
        List<Integer> input = Arrays.asList(1, 4, -1);
        List<List<String>> expected = Arrays.asList(List.of("last"), Arrays.asList("last", "review", "another", "one"), null);
        List<Boolean> shouldThrowException = Arrays.asList(false, false, true);
        List<Exception> exceptions = Arrays.asList(null, null, new IllegalArgumentException());

        for (int i = 0; i < input.size(); i++) {
            int inputValue = input.get(i);
            if (shouldThrowException.get(i)) {
                assertThrows(exceptions.get(i).getClass(), () -> {
                    analyzer.getMostPositiveWords
                            (inputValue);
                });
            } else {
                List<String> value = analyzer.getMostPositiveWords(inputValue);
                List<String> expectedValue = expected.get(i);
                assertEquals(Set.copyOf(expectedValue), Set.copyOf(value), "should calculate most positive words correctly");
            }
        }
    }

    @Test
    void getMostNegativeWords_WorksCorrectly() {
        List<Integer> input = Arrays.asList(1, 4, -1);
        List<List<String>> expected = Arrays.asList(List.of("negative"), Arrays.asList("negative", "here", "some", "there"), null);
        List<Boolean> shouldThrowException = Arrays.asList(false, false, true);
        List<Exception> exceptions = Arrays.asList(null, null, new IllegalArgumentException());

        for (int i = 0; i < input.size(); i++) {
            int inputValue = input.get(i);
            if (shouldThrowException.get(i)) {
                assertThrows(exceptions.get(i).getClass(), () -> {
                    analyzer.getMostNegativeWords(inputValue);
                });
            } else {
                List<String> value = analyzer.getMostNegativeWords(inputValue);
                List<String> expectedValue = expected.get(i);
                assertEquals(Set.copyOf(expectedValue), Set.copyOf(value), "should calculate most negative words correctly");
            }
        }
    }

    @Test
    void appendReview_HappyPath() {
        String rawReview = "Another interesting review here";
        int sentimentValue = (int) Sentiment.NEUTRAL.getValue();
        boolean isAppended = analyzer.appendReview(rawReview, sentimentValue);
        assertTrue(isAppended, "should append review successfully");
        assertEquals(
                String.format("%s%s", "2 Another interesting review here",
                        System.lineSeparator()),
                reviewWriter.toString(), "should write review correctly");
    }

    @Test
    void appendReview_ThrowsExceptionUponInvalidInput() {
        List<String> reviews = Arrays.asList(null, "", "some review");
        List<Integer> sentiments = Arrays.asList((int) Sentiment.POSITIVE.getValue(), (int) Sentiment.POSITIVE.getValue(), -1);
        List<Exception> exceptions = Arrays.asList(new IllegalArgumentException(), new IllegalArgumentException(), new IllegalArgumentException());

        for (int i = 0; i < reviews.size(); i++) {
            String review = reviews.get(i);
            int sentiment = sentiments.get(i);
            Exception exception = exceptions.get(i);
            assertThrows(exception.getClass(), () -> {
                analyzer.appendReview(review, sentiment);
            });

        }
    }

    @Test
    void appendReview_returnsFalseUponException() {
        String review = "some review";
        int sentiment = 1;
        Writer customWriter = new Writer() {
            @Override
            public void write(char[] chars, int i, int i1) throws IOException {
                throw new IOException("unimplemented");
            }

            @Override
            public void flush() throws IOException {
                throw new IOException("unimplemented");
            }

            @Override
            public void close() throws IOException {
            }
        };
        SentimentAnalyzer customAnalyzer = new MovieReviewSentimentAnalyzer(stopWordReader, reviewReader, customWriter);
        boolean result = customAnalyzer.appendReview(review, sentiment);
        assertFalse(result, "should fail to append review");
    }

    @Test
    void getSentimentDictionarySize() {
        assertEquals(8, analyzer.getSentimentDictionarySize(), "should calculate dictionary size correctly");
    }

    @Test
    void isStopWord() {
        List<String> input = Arrays.asList("the", "and", "is", "review", "nonexistent");
        List<Boolean> expected = Arrays.asList(true, true, true, false, false);
        for (int i = 0; i < input.size(); i++) {
            boolean value = analyzer.isStopWord(input.get(i));
            assertEquals(expected.get(i), value, "should calculate if a word is a stop word correctly");
        }
    }
}
