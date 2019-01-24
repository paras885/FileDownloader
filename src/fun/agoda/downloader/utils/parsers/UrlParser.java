package fun.agoda.downloader.utils.parsers;

import fun.agoda.downloader.pojos.UrlRequest;

@FunctionalInterface
public interface UrlParser {

    UrlRequest parse(final String url);
}
