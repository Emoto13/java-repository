package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.article.Article;

import java.util.Collection;
import java.util.List;

public interface ArticleSearcher {
    List<Article> searchArticlesBy(Collection<String> keywords, String country, String category);
}
