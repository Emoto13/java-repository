package bg.sofia.uni.fmi.mjt.news.url;

import bg.sofia.uni.fmi.mjt.news.verification.Verifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NewsUrlBuilder implements PaginatedUrlBuilder {
    public static final int PAGE_SIZE = 50;
    public static final String ENDPOINT = "https://newsapi.org/v2/top-headlines";
    private List<String> keywords;
    private String apiKey;
    private String country;
    private String category;
    private int page;

    public NewsUrlBuilder(String apiKey) {
        this.setApiKey(apiKey);
        this.keywords = new ArrayList<>();
        this.country = null;
        this.category = null;
        this.page = 1;
    }

    private void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Api key must not be empty or null");
        }
        this.apiKey = apiKey;
    }

    private String parameterOrNull(String parameterName, String value) {
        if (Verifier.isEmptyOrNull(value)) {
            return null;
        }
        return toParameter(parameterName, value);
    }

    private String toParameter(String parameterName, String value) {
        return String.format("%s=%s", parameterName, value);
    }

    @Override
    public NewsUrlBuilder reset() {
        this.country = null;
        this.category = null;
        this.page = 1;
        this.keywords = new ArrayList<>();
        return this;
    }

    public NewsUrlBuilder withNextPage() {
        this.page++;
        return this;
    }


    public NewsUrlBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public NewsUrlBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public NewsUrlBuilder withKeywords(Collection<String> keywords) {
        if (keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords have at least one element");
        } else if (Verifier.containsEmptyOrNull(keywords)) {
            throw new IllegalArgumentException("No keyword should be empty or null");
        }
        this.keywords.addAll(keywords);
        return this;
    }

    public int getSeenArticles() {
        return page * PAGE_SIZE;
    }

    public String build() {
        if (keywords.isEmpty()) {
            throw new IllegalStateException("No keywords provided");
        }

        String keywordsParameter = parameterOrNull(UrlParameter.KEYWORDS.getName(), String.join("+", keywords));
        String countryParameter = parameterOrNull(UrlParameter.COUNTRY.getName(), country);
        String categoryParameter = parameterOrNull(UrlParameter.CATEGORY.getName(), category);
        String apiKeyParameter = parameterOrNull(UrlParameter.API_KEY.getName(), apiKey);
        String pageParameter = toParameter(UrlParameter.PAGE.getName(), String.valueOf(page));
        String pageSizeParameter = toParameter(UrlParameter.PAGE_SIZE.getName(), String.valueOf(PAGE_SIZE));

        List<String> parameters = Stream.of(keywordsParameter, countryParameter, categoryParameter,
                        apiKeyParameter, pageParameter, pageSizeParameter)
                .filter((value) -> !Verifier.isEmptyOrNull(value)).toList();

        return String.format("%s?%s", NewsUrlBuilder.ENDPOINT, String.join("&", parameters));
    }
}
