package fun.agoda.downloader.bindingmodules;

import com.google.inject.AbstractModule;
import fun.agoda.downloader.FilesDownloader;
import fun.agoda.downloader.FilesDownloaderImpl;
import fun.agoda.downloader.concurrent.ThreadPoolExecutor;

public class ApplicationModule extends AbstractModule {

    // TODO: move to config.
    public final static int THREAD_POOL_SIZE_FOR_EXECUTOR = 10;

    @Override
    protected void configure() {
        bind(ThreadPoolExecutor.class).toInstance(new ThreadPoolExecutor(THREAD_POOL_SIZE_FOR_EXECUTOR));
        bind(FilesDownloader.class).to(FilesDownloaderImpl.class);
    }
}
