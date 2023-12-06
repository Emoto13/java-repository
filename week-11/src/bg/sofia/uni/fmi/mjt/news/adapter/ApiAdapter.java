package bg.sofia.uni.fmi.mjt.news.adapter;

import bg.sofia.uni.fmi.mjt.news.article.Article;
import bg.sofia.uni.fmi.mjt.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.mjt.news.exception.ApiError;

import java.util.List;

public interface ApiAdapter {
    List<Article> getArticles(GetArticlesParameters parameters) throws ApiError, FailedToRetrieveArticlesException;
}
