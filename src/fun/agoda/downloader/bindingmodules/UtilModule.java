package fun.agoda.downloader.bindingmodules;

import com.google.inject.AbstractModule;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.manipulators.FileManipulatorImpl;
import fun.agoda.downloader.utils.parsers.UrlParser;
import fun.agoda.downloader.utils.parsers.UrlParserImpl;
import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrer;
import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrerByReadableByteChannel;

public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UrlParser.class).to(UrlParserImpl.class);
        bind(FileManipulator.class).to(FileManipulatorImpl.class);
        bind(UrlToFileTransferrer.class).to(UrlToFileTransferrerByReadableByteChannel.class);
    }
}
