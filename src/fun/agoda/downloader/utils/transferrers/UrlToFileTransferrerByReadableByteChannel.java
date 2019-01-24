package fun.agoda.downloader.utils.transferrers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class UrlToFileTransferrerByReadableByteChannel implements UrlToFileTransferrer {

    @Override
    public void transfer(final URL url, final File fileToSaveData) throws IOException {
        try (final ReadableByteChannel channel = getChannelByUrl(url);
             final FileOutputStream fileOutputStream = createOutputStreamForFile(fileToSaveData)) {
                final FileChannel fileChannel = fileOutputStream.getChannel();
                fileChannel.transferFrom(channel, 0, Long.MAX_VALUE);
        }
    }

    private ReadableByteChannel getChannelByUrl(final URL url) throws IOException {
        final InputStream inputStream = url.openStream();
        return Channels.newChannel(inputStream);
    }

    private FileOutputStream createOutputStreamForFile(final File fileToSaveData) throws IOException {
        return new FileOutputStream(fileToSaveData);
    }
}
