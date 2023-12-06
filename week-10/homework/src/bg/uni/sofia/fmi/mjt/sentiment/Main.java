package bg.uni.sofia.fmi.mjt.sentiment;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String base = "~/Desktop/modern-java-technologies-2022-2023/week-10/homework/";
        Reader stop = new BufferedReader(new FileReader("resources/stopwords.txt"));
        Reader word = new BufferedReader(new FileReader("resources//reviews.txt"));
        Writer w = new FileWriter("resources/reviews.txt", true);
        SentimentAnalyzer sentimentAnalyzer = new MovieReviewSentimentAnalyzer(stop, word, w);
        String review = "And here   is some_really,weird review-man";
        sentimentAnalyzer.appendReview(review, 3);
        System.out.println(sentimentAnalyzer.getReviewSentiment(review));
        System.out.println(sentimentAnalyzer.getReviewSentimentAsName(review));

        word.close();
        w.close();
        stop.close();
    }
}
