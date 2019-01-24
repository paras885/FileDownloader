package fun.agoda.downloader.exceptions;

public class FileIteratorNonRetryableException extends RuntimeException {

    public FileIteratorNonRetryableException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }
}
