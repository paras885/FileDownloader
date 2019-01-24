package fun.agoda.downloader.utils.iterators;

import fun.agoda.downloader.exceptions.FileIteratorNonRetryableException;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class FileIteratorTest {

    private final static String VALID_INPUT_FILE = "tst/sampledata/inputFile.txt";
    private final static String INVALID_INPUT_FILE = "tst/sampledata/invalid.txt";

    @Test
    public void testHappyPath_whenFileIsValid() {
        final Iterator<String> fileIterator = FileIterator.of(VALID_INPUT_FILE);
        int numberOfLines = 0;
        while (fileIterator.hasNext()) {
            numberOfLines ++;
            fileIterator.next();
        }

        assertThat(numberOfLines, is(1));
    }

    @Test(expected = FileIteratorNonRetryableException.class)
    public void testFlow_whenFileNotFound_shouldThrowFileIteratorNonRetryableException() {
        FileIterator.of(INVALID_INPUT_FILE);
    }
}
