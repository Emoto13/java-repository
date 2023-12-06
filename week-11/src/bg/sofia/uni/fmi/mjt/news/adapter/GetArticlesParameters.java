package bg.sofia.uni.fmi.mjt.news.adapter;

import java.util.Collection;

public record GetArticlesParameters(Collection<String> keywords, String country, String category) {
}
