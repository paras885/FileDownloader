package fun.agoda.downloader.businesslogics;

import fun.agoda.downloader.pojos.UrlRequest;

import java.io.File;

@FunctionalInterface
public interface BusinessLogic {

    void apply(final UrlRequest urlRequest, final String directoryPathToSaveFile);
}
