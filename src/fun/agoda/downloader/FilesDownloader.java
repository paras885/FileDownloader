package fun.agoda.downloader;

import fun.agoda.downloader.pojos.FilesDownloaderRequest;

@FunctionalInterface
public interface FilesDownloader {

    void download(final FilesDownloaderRequest filesDownloaderRequest);
}
