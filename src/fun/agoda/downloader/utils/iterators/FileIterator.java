package fun.agoda.downloader.utils.iterators;

import fun.agoda.downloader.exceptions.FileIteratorNonRetryableException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Builder
@Slf4j
public class FileIterator implements Iterator<String> {

    private String filePath;
    private BufferedReader bufferedReader;
    private String nextLine;

    public static Iterator<String> of(final String filePath) {
        final FileIterator fileIteratorInstance = FileIterator.builder()
                .filePath(filePath)
                .build();
        fileIteratorInstance.initializeIterator();

        return fileIteratorInstance;
    }

    private void initializeIterator() {
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (final FileNotFoundException fileNotFoundException) {
            final String errorMessage = String.format("File: %s not found.", filePath);
            log.error(errorMessage);

            throw new FileIteratorNonRetryableException(errorMessage, fileNotFoundException);
        }
    }

    public boolean hasNext() {
        readNextLine();
        return nextLine != null;
    }

    private void readNextLine() {
        try {
            nextLine = bufferedReader.readLine();
        } catch (final IOException ioException) {
            final String errorMessage = String.format("Unexpected error, while reading file: %s.", filePath);
            log.error(errorMessage);

            throw new FileIteratorNonRetryableException(errorMessage, ioException);
        }
    }

    public String next() {
        return nextLine;
    }
}
