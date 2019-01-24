package fun.agoda.downloader.utils.manipulators;

import java.io.File;
import java.util.Optional;

public interface FileManipulator {

    Optional<File> getFileFromPath(final String filePath);

    File createFile(final String filePath);

    File createFileInDirectory(final String fileName, final File parentDirectory);


}
