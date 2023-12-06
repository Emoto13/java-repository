package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.adapter.ApiAdapter;
import bg.sofia.uni.fmi.mjt.news.adapter.GetArticlesParameters;
import bg.sofia.uni.fmi.mjt.news.adapter.NewApiAdapter;
import bg.sofia.uni.fmi.mjt.news.url.NewsUrlBuilder;
import bg.sofia.uni.fmi.mjt.news.url.PaginatedUrlBuilder;
import bg.sofia.uni.fmi.mjt.news.article.Article;
import bg.sofia.uni.fmi.mjt.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.mjt.news.exception.ApiError;

import java.net.http.HttpClient;
import java.util.Collection;
import java.util.List;

public class NewsArticleSearcher implements ArticleSearcher {
    private ApiAdapter apiAdapter;

    public NewsArticleSearcher(String apiKey) {
        this.createApiAdapter(apiKey);
    }

    private void createApiAdapter(String apiKey) {
        PaginatedUrlBuilder urlBuilder = new NewsUrlBuilder(apiKey);
        this.apiAdapter = new NewApiAdapter(urlBuilder, HttpClient.newHttpClient());
    }

    public List<Article> searchArticlesBy(Collection<String> keywords, String country, String category) {
        if (keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords should not be empty");
        }
        GetArticlesParameters parameters = new GetArticlesParameters(keywords, country, category);
        try {
            return apiAdapter.getArticles(parameters);
        } catch (FailedToRetrieveArticlesException | ApiError error) {
            System.err.println(error.getMessage());
        }
        return null;
    }
}
