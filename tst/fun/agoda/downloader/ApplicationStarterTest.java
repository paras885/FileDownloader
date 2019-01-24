package fun.agoda.downloader;

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.spi.Message;
import fun.agoda.downloader.exceptions.FileIteratorNonRetryableException;
import fun.agoda.downloader.pojos.FilesDownloaderRequest;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;

@RunWith(JMockit.class)
public class ApplicationStarterTest {

    private final static String DEFAULT_INPUT_FILE = "tst/sampledata/inputFile.txt";
    private final static String CURRENT_PATH = ".";

    @Mocked
    private FilesDownloader filesDownloader;

    @Mocked
    private Injector injector;

    @Before
    public void setup() {
        new MockUp<Guice>() {
            @Mock
            public Injector createInjector(Module... modules) {
                return injector;
            }
        };
    }

    @Test
    public void testHappyPathWithDefault_filesDownloader_shouldGetExpectedInput_andExecuteSuccessfully() {
        new Expectations() {
            {
                injector.getInstance(FilesDownloader.class);
                result = filesDownloader;
                times = 1;
            }
        };

        ApplicationStarter.main(new String[0]);

        final FilesDownloaderRequest expectedDownloaderRequest = FilesDownloaderRequest.builder()
                .inputFile(DEFAULT_INPUT_FILE)
                .directoryPathToSaveData(CURRENT_PATH)
                .build();
        new Verifications() {
            {
                filesDownloader.download(expectedDownloaderRequest);
                times = 1;
            }
        };
    }

    @Test
    public void testHappyPathWithCommandLineArgs_filesDownloader_shouldGetExpectedInput_andExecuteSuccessfully() {
        new Expectations() {
            {
                injector.getInstance(FilesDownloader.class);
                result = filesDownloader;
                times = 1;
            }
        };

        final String userSpecificInputFile = "inputFile";
        final String userSpecificDirectoryToSave = "directoryToSave";
        final String[] commandLineArgs = new String[] {userSpecificInputFile, userSpecificDirectoryToSave};
        ApplicationStarter.main(commandLineArgs);

        final FilesDownloaderRequest expectedDownloaderRequest = FilesDownloaderRequest.builder()
                .inputFile(userSpecificInputFile)
                .directoryPathToSaveData(userSpecificDirectoryToSave)
                .build();
        new Verifications() {
            {
                filesDownloader.download(expectedDownloaderRequest);
                times = 1;
            }
        };
    }

    @Test(expected = CreationException.class)
    public void testPath_whenGuiceFailToInjectDependencies_shouldThrowCreationException() {
        new MockUp<Guice>() {
            @Mock
            public Injector createInjector(Module... modules) {
                final Message message = new Message("Failed to inject");
                final Collection<Message> messages = new HashSet<>();
                messages.add(message);

                throw new CreationException(messages);
            }
        };

        ApplicationStarter.main(new String[0]);
    }

    @Test(expected = FileIteratorNonRetryableException.class)
    public void testPath_whenFilesDownloaderFailToFindInputFile_shouldThrowFileIteratorNonRetryableException() {
        new Expectations() {
            {
                injector.getInstance(FilesDownloader.class);
                result = filesDownloader;
                times = 1;

                filesDownloader.download((FilesDownloaderRequest) any);
                result = new FileIteratorNonRetryableException("Failed to find file", new FileNotFoundException());
                times = 1;
            }
        };

        ApplicationStarter.main(new String[0]);
    }

    @Test(expected = SecurityException.class)
    public void testPath_whenFilesDownloaderFailToShutdownThreadWorkers_shouldThrowSecurityException() {
        new Expectations() {
            {
                injector.getInstance(FilesDownloader.class);
                result = filesDownloader;
                times = 1;

                filesDownloader.download((FilesDownloaderRequest) any);
                result = new SecurityException();
                times = 1;
            }
        };

        ApplicationStarter.main(new String[0]);
    }
}
