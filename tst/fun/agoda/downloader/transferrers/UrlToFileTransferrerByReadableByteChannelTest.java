package fun.agoda.downloader.transferrers;

import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrerByReadableByteChannel;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@RunWith(JMockit.class)
public class UrlToFileTransferrerByReadableByteChannelTest {

    @Tested
    private UrlToFileTransferrerByReadableByteChannel transferrer;

    @Mocked private URL url;
    @Mocked private InputStream inputStream;
    @Mocked private Channels channels;
    @Mocked private ReadableByteChannel readableByteChannel;
    @Mocked private FileChannel fileChannel;
    @Mocked private File fileToSaveData;

    @Test
    public void testTransfer_happyPath_shouldTransferFileSuccessfully(@Mocked final FileOutputStream fileOutputStream)
            throws IOException {
        new Expectations() {
            {
                url.openStream();
                result = inputStream;
                times = 1;

                Channels.newChannel(inputStream);
                result = readableByteChannel;
                times = 1;

                new FileOutputStream(fileToSaveData);
                result = fileOutputStream;
                times = 1;

                fileOutputStream.getChannel();
                result = fileChannel;
                times = 1;

                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                times = 1;

                fileOutputStream.close();
                times = 1;

                readableByteChannel.close();
                times = 1;
            }
        };

        transferrer.transfer(url, fileToSaveData);
    }

    @Test(expected = IOException.class)
    public void testTransfer_whenUrlFailToOpenDataStream_shouldThrowIOException() throws IOException {
        new Expectations() {
            {
                url.openStream();
                result = new IOException();
                times = 1;
            }
        };

        transferrer.transfer(url, fileToSaveData);
    }

    @Test(expected = IOException.class)
    public void testTransfer_whenFileChannelFailToTransferData_shouldThrowIOException(
            @Mocked final FileOutputStream fileOutputStream) throws IOException {
        new Expectations() {
            {
                url.openStream();
                result = inputStream;
                times = 1;

                Channels.newChannel(inputStream);
                result = readableByteChannel;
                times = 1;

                new FileOutputStream(fileToSaveData);
                result = fileOutputStream;
                times = 1;

                fileOutputStream.getChannel();
                result = fileChannel;
                times = 1;

                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                result = new IOException();
                times = 1;

                fileOutputStream.close();
                times = 1;

                readableByteChannel.close();
                times = 1;
            }
        };

        transferrer.transfer(url, fileToSaveData);
    }
}
