package fun.agoda.downloader.businesslogics;

import fun.agoda.downloader.pojos.UrlRequest;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

// TODO: Break downloading of single file into multiple files using position offset and then in background, worker merge these chunks.
@Slf4j
public abstract class AbstractBusinessLogic implements BusinessLogic {

    protected FileManipulator fileManipulator;
    protected UrlToFileTransferrer channelToFileTransferrer;

    @Override
    public void apply(final UrlRequest urlRequest, final String directoryPathToSaveData) {
        final File protocolSpecificDirectory = getFileByPath(directoryPathToSaveData);

        final Optional<URL> url = extractUrlFromRequest(urlRequest);
        if (url.isPresent()) {
            log.info("Downloading file: [{}]", url.get());

            final String fileName = urlRequest.getSystemSpecificFileNameFromUrl();
            final File fileToSaveData = fileManipulator.createFileInDirectory(fileName, protocolSpecificDirectory);
            try {
                channelToFileTransferrer.transfer(url.get(), fileToSaveData);
                log.info("File: [{}] successfully downloaded into path: [{}].", url.get(), fileToSaveData);
            } catch (final IOException ioException) {
                log.error("Unexpected error occurred, while downloading file: [{}], cause: {}",
                        urlRequest.getUrlFilePath(), ioException.getStackTrace());
                removePartialDownloadedFile(fileToSaveData);
            }
        }
    }

    private File getFileByPath(String directoryPathToSaveData) {
        return fileManipulator.getFileFromPath(directoryPathToSaveData)
                .orElseGet(() -> fileManipulator.createFile(directoryPathToSaveData));
    }

    protected abstract Optional<URL> extractUrlFromRequest(final UrlRequest urlRequest);

    private void removePartialDownloadedFile(final File partiallyDownloadedFile) {
        try {
            Files.delete(partiallyDownloadedFile.toPath());
            log.info("Successfully deleted partially downloaded file: [{}]", partiallyDownloadedFile);
        } catch (final IOException ioException) {
            log.error("Unable to delete partially downloaded file: [{}], cause: {}", partiallyDownloadedFile,
                    ioException);
        }
    }
}
