package fun.agoda.downloader;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.concurrent.BusinessLogicRunnable;
import fun.agoda.downloader.concurrent.ThreadPoolExecutor;
import fun.agoda.downloader.factories.BusinessLogicFactory;
import fun.agoda.downloader.pojos.FilesDownloaderRequest;
import fun.agoda.downloader.pojos.Protocol;
import fun.agoda.downloader.pojos.UrlRequest;
import fun.agoda.downloader.utils.iterators.FileIterator;
import fun.agoda.downloader.utils.iterators.Iterator;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.parsers.UrlParser;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
public class FilesDownloaderImpl implements FilesDownloader {

    private BusinessLogicFactory businessLogicFactory;
    private UrlParser urlParser;
    private ThreadPoolExecutor threadPoolExecutor;
    private FileManipulator fileManipulator;

    @Inject
    public FilesDownloaderImpl(final BusinessLogicFactory businessLogicFactory, final UrlParser urlParser,
                               final ThreadPoolExecutor threadPoolExecutor, final FileManipulator fileManipulator) {
        this.businessLogicFactory = businessLogicFactory;
        this.urlParser = urlParser;
        this.threadPoolExecutor = threadPoolExecutor;
        this.fileManipulator = fileManipulator;
    }

    @Override
    public void download(final FilesDownloaderRequest filesDownloaderRequest) {
        final Iterator<String> fileIterator = FileIterator.of(filesDownloaderRequest.getInputFile());
        while (fileIterator.hasNext()) {
            final String url = fileIterator.next();
            try {
                final UrlRequest urlRequest = urlParser.parse(url);
                final Runnable runnableTask = getRunnableBusinessTask(urlRequest, filesDownloaderRequest);
                threadPoolExecutor.registerNewTask(runnableTask);
            } catch (final IllegalArgumentException | RejectedExecutionException | NullPointerException exception) {
                log.error("Skipping url: [{}], cause: {}", url, exception);
            }
        }

        threadPoolExecutor.waitToExecuteAllTasks();
    }

    private Runnable getRunnableBusinessTask(final UrlRequest urlRequest,
                                             final FilesDownloaderRequest filesDownloaderRequest) {
        final BusinessLogic businessLogic = businessLogicFactory.getBusinessLogicByProtocol(urlRequest.getProtocol());
        final String protocolSpecificDirectoryPath = createProtocolSpecificDirectoryPath(urlRequest.getProtocol(),
                filesDownloaderRequest.getDirectoryPathToSaveData());
        final Runnable runnableTask = BusinessLogicRunnable.builder()
                .urlRequest(urlRequest)
                .businessLogic(businessLogic)
                .protocolDirectoryPath(protocolSpecificDirectoryPath)
                .build();

        return runnableTask;
    }

    private String createProtocolSpecificDirectoryPath(final Protocol protocol, final String parentDirectoryPath) {
        return String.format("%s%s%s", parentDirectoryPath, File.separator, protocol.toString());
    }
}
