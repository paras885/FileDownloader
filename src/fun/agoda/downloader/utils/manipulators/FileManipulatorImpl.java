package fun.agoda.downloader.utils.manipulators;

import java.io.File;
import java.util.Optional;

public class FileManipulatorImpl implements FileManipulator {

    @Override
    public Optional<File> getFileFromPath(final String filePath) {
        final File file = new File(filePath);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }

    @Override
    public File createFile(final String filePath) {
        final File file = new File(filePath);
        file.mkdir();

        return file;
    }

    @Override
    public File createFileInDirectory(final String fileName, final File parentDirectory) {
        return new File(parentDirectory, fileName);
    }
}
