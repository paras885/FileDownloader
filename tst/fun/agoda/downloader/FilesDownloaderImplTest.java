package fun.agoda.downloader;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.concurrent.ThreadPoolExecutor;
import fun.agoda.downloader.exceptions.FileIteratorNonRetryableException;
import fun.agoda.downloader.factories.BusinessLogicFactory;
import fun.agoda.downloader.pojos.FilesDownloaderRequest;
import fun.agoda.downloader.pojos.Protocol;
import fun.agoda.downloader.pojos.UrlRequest;
import fun.agoda.downloader.utils.iterators.FileIterator;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.parsers.UrlParser;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RunWith(JMockit.class)
public class FilesDownloaderImplTest {

    private final static String URL = "url";
    private final static Protocol PROTOCOL = Protocol.HTTP;
    private final static String URL_FILE_PATH = "filePath";
    private final static String INPUT_FILE = "inputFile";
    private final static String DIRECTORY_PATH_TO_SAVE_DATA = "directoryPath";
    private final static Map<String, String> ADDITIONAL_ATTRIBUTES = new HashMap<>();

    @Tested
    private FilesDownloaderImpl filesDownloader;

    @Injectable private BusinessLogicFactory businessLogicFactory;
    @Injectable private UrlParser urlParser;
    @Injectable private ThreadPoolExecutor threadPoolExecutor;
    @Injectable private FileManipulator fileManipulator;

    @Mocked private BusinessLogic businessLogic;
    @Mocked private File protocolSpecificDirectory;
    @Mocked private Path pathToSaveData;
    @Mocked private FileIterator fileIterator;

    @Test
    public void testDownloadHappyPath_whenFileIterator_urlParser_businessFactoryReceiveExpectedInput() {
        final UrlRequest urlRequest = createUrlRequest();
        final FilesDownloaderRequest filesDownloaderRequest = createFilesDownloaderRequest();
        new Expectations() {
            {
                FileIterator.of(filesDownloaderRequest.getInputFile());
                result = fileIterator;
                times = 1;

                fileIterator.hasNext();
                result = true;

                fileIterator.next();
                result = URL;

                urlParser.parse(URL);
                result = urlRequest;
                times = 1;

                businessLogicFactory.getBusinessLogicByProtocol(urlRequest.getProtocol());
                result = businessLogic;
                times = 1;

                fileIterator.hasNext();
                result = false;
            }
        };

        filesDownloader.download(filesDownloaderRequest);
    }

    @Test(expected = FileIteratorNonRetryableException.class)
    public void testDownload_whenFileItratorFailToFoundInputFile_shouldThrowFileIteratorNonRetryableException() {
        final FilesDownloaderRequest filesDownloaderRequest = createFilesDownloaderRequest();
        new Expectations() {
            {
                FileIterator.of(filesDownloaderRequest.getInputFile());
                result = new FileIteratorNonRetryableException("File not found.", new FileNotFoundException());
                times = 1;
            }
        };

        filesDownloader.download(filesDownloaderRequest);
    }

    @Test(expected = FileIteratorNonRetryableException.class)
    public void testDownload_whenFileIteratorFailToReadInputFile_shouldThrowFileIteratorNonRetryableException() {
        final FilesDownloaderRequest filesDownloaderRequest = createFilesDownloaderRequest();
        new Expectations() {
            {
                FileIterator.of(filesDownloaderRequest.getInputFile());
                result = new FileIteratorNonRetryableException("Unexpected exception", new IOException());
                times = 1;
            }
        };

        filesDownloader.download(filesDownloaderRequest);
    }

    @Test(expected = AssertionError.class)
    public void testDownload_forCorruptFiles_whenUrlParserFailToParseUrl_shouldThrowAssertionError() {
        final FilesDownloaderRequest filesDownloaderRequest = createFilesDownloaderRequest();
        new Expectations() {
            {
                FileIterator.of(filesDownloaderRequest.getInputFile());
                result = fileIterator;
                times = 1;

                fileIterator.hasNext();
                result = true;

                fileIterator.next();
                result = URL;

                urlParser.parse(URL);
                result = new AssertionError();
            }
        };

        filesDownloader.download(filesDownloaderRequest);
    }

    @Test
    public void testDownload_whenBusinessFactoryFailBecauseIllegalArgument_shouldSkipUrl() {
        final UrlRequest urlRequest = createUrlRequest();
        final FilesDownloaderRequest filesDownloaderRequest = createFilesDownloaderRequest();
        new Expectations() {
            {
                FileIterator.of(filesDownloaderRequest.getInputFile());
                result = fileIterator;
                times = 1;

                fileIterator.hasNext();
                result = true;

                fileIterator.next();
                result = URL;

                urlParser.parse(URL);
                result = urlRequest;
                times = 1;

                businessLogicFactory.getBusinessLogicByProtocol(urlRequest.getProtocol());
                result = new IllegalArgumentException();
                times = 1;

                fileIterator.hasNext();
                result = false;
            }
        };

        filesDownloader.download(filesDownloaderRequest);
    }

    private UrlRequest createUrlRequest() {
        return UrlRequest.builder()
                .protocol(PROTOCOL)
                .urlFilePath(URL_FILE_PATH)
                .additionalAttributes(ADDITIONAL_ATTRIBUTES)
                .build();
    }

    private FilesDownloaderRequest createFilesDownloaderRequest() {
        return FilesDownloaderRequest.builder()
                .inputFile(INPUT_FILE)
                .directoryPathToSaveData(DIRECTORY_PATH_TO_SAVE_DATA)
                .build();
    }
}
