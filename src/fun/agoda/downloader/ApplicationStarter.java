package fun.agoda.downloader;

import com.google.inject.Injector;
import fun.agoda.downloader.bindingmodules.UtilModule;
import fun.agoda.downloader.bindingmodules.ApplicationModule;
import fun.agoda.downloader.bindingmodules.BusinessLogicFactoryModule;
import fun.agoda.downloader.pojos.FilesDownloaderRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.google.inject.Guice.createInjector;

// TODO: Parsing argument is not implemented in extensible way, need to revisit and add argument parser.
@Slf4j
public class ApplicationStarter {

    private final static String DEFAULT_INPUT_FILE = "tst/sampledata/inputFile.txt";
    private final static String CURRENT_PATH = ".";

    public static void main(final String[] args) {
        log.info("Application started with args: {}", Arrays.toString(args));

        final Injector injector = injectDependencies();
        final FilesDownloaderRequest request = parseArgument(args);
        final FilesDownloader filesDownloader = injector.getInstance(FilesDownloader.class);
        filesDownloader.download(request);
    }

    private static Injector injectDependencies() {
        return createInjector(new ApplicationModule(), new BusinessLogicFactoryModule(), new UtilModule());
    }

    private static FilesDownloaderRequest parseArgument(final String[] args) {
        final String inputFile = getInputFile(args);
        final String pathToSaveData = getPathToSaveData(args);
        return FilesDownloaderRequest.builder()
                .inputFile(inputFile)
                .directoryPathToSaveData(pathToSaveData)
                .build();
    }

    private static String getInputFile(final String[] args) {
        if (args.length > 0) {
            return args[0];
        } else {
            return DEFAULT_INPUT_FILE;
        }
    }

    private static String getPathToSaveData(final String[] args) {
        if (args.length > 1) {
            return args[1];
        } else {
            return CURRENT_PATH;
        }
    }
}
