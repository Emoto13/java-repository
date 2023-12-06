package bg.uni.sofia.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Comparator;


public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {
    private final Writer reviewWriter;
    private Set<String> stopWords;
    private Map<String, WordSentiment> wordToWordSentiment;
    private Set<WordSentiment> words;

    public MovieReviewSentimentAnalyzer(Reader stopWordsIn, Reader reviewsIn, Writer reviewsOut) {
        this.createStopWords(stopWordsIn);
        this.createWordSentiment(reviewsIn);
        this.reviewWriter = reviewsOut;
    }

    private void createStopWords(Reader stopWordsIn) {
        this.stopWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(stopWordsIn);) {
            String line = reader.readLine();
            while (line != null) {
                this.stopWords.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWordSentiment(Reader reviewsIn) {
        this.wordToWordSentiment = new HashMap<>();
        this.words = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(reviewsIn)) {
            String rawReview = reader.readLine();
            while (rawReview != null) {
                Review review = Review.toReview(rawReview);
                addReview(review);
                rawReview = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAWord(String word) {
        return word.matches("[A-z']{2,}");
    }

    public void addSentimentToWord(String token, double sentiment) {
        if (!wordToWordSentiment.containsKey(token)) {
            WordSentiment wordSentiment = new WordSentiment(token, 0.0, 0);
            wordToWordSentiment.put(token, wordSentiment);
            words.add(wordSentiment);
        }
        WordSentiment wordSentiment = wordToWordSentiment.get(token);
        wordSentiment.addSentiment(sentiment);
    }

    public void addReview(Review review) {
        Set<String> seenWords = new HashSet<>();
        for (String token : review.getWords()) {
            if (!isAWord(token) || isStopWord(token) || seenWords.contains(token)) {
                continue;
            }

            addSentimentToWord(token, review.getSentiment());
            seenWords.add(token);
        }
    }

    @Override
    public double getReviewSentiment(String review) {
        String[] words = Review.reviewToWords(review);
        double totalSentiment = 0.0;
        int matchedWords = 0;
        for (String word : words) {
            if (isStopWord(word) || !wordToWordSentiment.containsKey(word)) {
                continue;
            }
            matchedWords++;
            totalSentiment += getWordSentiment(word);
        }

        if (matchedWords == 0) {
            return Sentiment.UNKNOWN.getValue();
        }
        return totalSentiment / matchedWords;
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        int sentiment = (int) Math.round(getReviewSentiment(review));
        return SentimentFactory.createSentiment(sentiment).getName();
    }

    @Override
    public double getWordSentiment(String rawWord) {
        String word = rawWord.toLowerCase();
        if (!wordToWordSentiment.containsKey(word)) {
            return Sentiment.UNKNOWN.getValue();
        }
        return wordToWordSentiment.get(word).getSentiment();
    }

    @Override
    public int getWordFrequency(String word) {
        if (!wordToWordSentiment.containsKey(word)) {
            return 0;
        }
        return wordToWordSentiment.get(word).getFrequency();
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        return words.stream()
                .sorted(Comparator.comparing(WordSentiment::getFrequency).reversed())
                .map(WordSentiment::getWord)
                .limit(n)
                .toList();
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

        return words.stream()
                .sorted(Comparator.comparing(WordSentiment::getSentiment).reversed())
                .map(WordSentiment::getWord)
                .limit(n)
                .toList();
    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

        return words.stream()
                .sorted(Comparator.comparing(WordSentiment::getSentiment))
                .map(WordSentiment::getWord)
                .limit(n)
                .toList();
    }

    @Override
    public boolean appendReview(String review, int sentiment) {
        if (review == null || review.isBlank()) {
            throw new IllegalArgumentException("Invalid review provided");
        } else if (sentiment < Sentiment.LOW_BOUNDARY ||
                sentiment > Sentiment.HIGH_BOUNDARY) {
            throw new IllegalArgumentException("Invalid sentiment provided");
        }

        Review fullReview = new Review(sentiment, review);
        addReview(fullReview);
        try {
            reviewWriter.write(String.valueOf(sentiment));
            reviewWriter.write(" ");
            reviewWriter.write(review);
            reviewWriter.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int getSentimentDictionarySize() {
        return wordToWordSentiment.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}
