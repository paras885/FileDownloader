package fun.agoda.downloader.utils.manipulators;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JMockit.class)
public class FileManipulatorImplTest {

    private final static String FILE_PATH = "filePath";

    @Tested
    private FileManipulatorImpl fileManipulator;

    @Mocked
    private File file;

    @Test
    public void testGetFileFromPath_whenFileExist() {
        new Expectations() {
            {
                new File(FILE_PATH);
                result = file;
                times = 1;

                file.exists();
                result = true;
                times = 1;
            }
        };

        final Optional<File> file = fileManipulator.getFileFromPath(FILE_PATH);
        assertThat(file.isPresent(), is(true));
    }

    @Test
    public void testGetFile_whenFileDoesNotExist() {
        new Expectations() {
            {
                new File(FILE_PATH);
                result = file;
                times = 1;

                file.exists();
                result = false;
                times = 1;
            }
        };

        final Optional<File> file = fileManipulator.getFileFromPath(FILE_PATH);
        assertThat(file.isPresent(), is(false));
    }
}
