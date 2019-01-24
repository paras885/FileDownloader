package fun.agoda.downloader.utils.transferrers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@FunctionalInterface
public interface UrlToFileTransferrer {

    void transfer(final URL url, final File fileToSaveData) throws IOException;
}
