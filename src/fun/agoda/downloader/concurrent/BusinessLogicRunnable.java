package fun.agoda.downloader.concurrent;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.pojos.UrlRequest;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class BusinessLogicRunnable implements Runnable {

    private BusinessLogic businessLogic;
    private UrlRequest urlRequest;
    private String protocolDirectoryPath;

    @Override
    public void run() {
        try {
            businessLogic.apply(urlRequest, protocolDirectoryPath);
        } catch (Exception exception) {
            log.error("Failed to download file: [{}] skipping it, cause: {}.", urlRequest.getUrlFilePath(), exception);
        }
    }
}
