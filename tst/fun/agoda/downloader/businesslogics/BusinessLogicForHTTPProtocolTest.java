package fun.agoda.downloader.businesslogics;

import fun.agoda.downloader.pojos.Protocol;
import fun.agoda.downloader.pojos.UrlRequest;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrer;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class BusinessLogicForHTTPProtocolTest {

    private final static String URL_FILE_PATH = "filePath";
    private final static String URL_FILE_PATH_WITH_PROTOCOL = "http://filePath";

    @Tested
    private BusinessLogicForHTTPProtocol businessLogic;

    @Injectable private FileManipulator fileManipulator;
    @Injectable private UrlToFileTransferrer transferrer;

    @Mocked private File fileToSaveData;
    @Mocked private URL url;
    @Mocked private Files files;

    @Test
    public void testApply_happyPath_whenProtocolDirectoryExist_shouldSuccessfullyDownloadFile() throws IOException {
        final UrlRequest urlRequest = createUrlRequest();
        final String directoryToSaveData = "directory";
        final Optional<File> protocolSpecificDirectory = Optional.of(new File(directoryToSaveData));
        new Expectations() {
            {
                fileManipulator.getFileFromPath(directoryToSaveData);
                result = protocolSpecificDirectory;
                times = 1;

                new URL(URL_FILE_PATH_WITH_PROTOCOL);
                result = url;
                times = 1;

                fileManipulator.createFileInDirectory(urlRequest.getSystemSpecificFileNameFromUrl(),
                        protocolSpecificDirectory.get());
                result = fileToSaveData;
                times = 1;

                transferrer.transfer(url, fileToSaveData);
                times = 1;
            }
        };

        businessLogic.apply(urlRequest, directoryToSaveData);
    }

    @Test
    public void testApply_happyPath_whenProtocolDirectoryDoesNotExist_shouldSuccessfullyDownloadFile()
            throws IOException {
        final UrlRequest urlRequest = createUrlRequest();
        final String directoryToSaveData = "directory";
        final File protocolSpecificDirectory = new File(directoryToSaveData);
        new Expectations() {
            {
                fileManipulator.getFileFromPath(directoryToSaveData);
                result = Optional.empty();
                times = 1;

                fileManipulator.createFile(directoryToSaveData);
                result = protocolSpecificDirectory;
                times = 1;

                new URL(URL_FILE_PATH_WITH_PROTOCOL);
                result = url;
                times = 1;

                fileManipulator.createFileInDirectory(urlRequest.getSystemSpecificFileNameFromUrl(),
                        protocolSpecificDirectory);
                result = fileToSaveData;
                times = 1;

                transferrer.transfer(url, fileToSaveData);
                times = 1;
            }
        };

        businessLogic.apply(urlRequest, directoryToSaveData);
    }

    @Test
    public void testApply_whenTransferrerFailToTransferDataBetweenChannels_shouldRemovePartiallyDownloadedFile()
            throws IOException {
        final UrlRequest urlRequest = createUrlRequest();
        final String directoryToSaveData = "directory";
        final Optional<File> protocolSpecificDirectory = Optional.of(new File(directoryToSaveData));
        new Expectations() {
            {
                fileManipulator.getFileFromPath(directoryToSaveData);
                result = protocolSpecificDirectory;
                times = 1;

                new URL(URL_FILE_PATH_WITH_PROTOCOL);
                result = url;
                times = 1;

                fileManipulator.createFileInDirectory(urlRequest.getSystemSpecificFileNameFromUrl(),
                        protocolSpecificDirectory.get());
                result = fileToSaveData;
                times = 1;

                transferrer.transfer(url, fileToSaveData);
                result = new IOException();
                times = 1;

                files.delete((Path) any);
                times = 1;
            }
        };

        businessLogic.apply(urlRequest, directoryToSaveData);
    }

    private UrlRequest createUrlRequest() {
        return UrlRequest.builder()
                .urlFilePath(URL_FILE_PATH)
                .protocol(Protocol.HTTP)
                .build();
    }
}
