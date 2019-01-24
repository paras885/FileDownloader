package fun.agoda.downloader.pojos;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Builder
@Data
public class FilesDownloaderRequest {

    private String inputFile;
    private String directoryPathToSaveData;
}
