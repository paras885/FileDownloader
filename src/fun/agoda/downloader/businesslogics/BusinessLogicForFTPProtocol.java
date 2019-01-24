package fun.agoda.downloader.businesslogics;

import fun.agoda.downloader.pojos.UrlRequest;
import fun.agoda.downloader.utils.manipulators.FileManipulator;
import fun.agoda.downloader.utils.transferrers.UrlToFileTransferrer;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Slf4j
public class BusinessLogicForFTPProtocol extends AbstractBusinessLogic {

    @Inject
    public BusinessLogicForFTPProtocol(final FileManipulator fileManipulator,
                                        final UrlToFileTransferrer channelToFileTransferrer) {
        super.fileManipulator = fileManipulator;
        super.channelToFileTransferrer = channelToFileTransferrer;
    }

    @Override
    protected Optional<URL> extractUrlFromRequest(UrlRequest urlRequest) {
        final Optional<String> userName = urlRequest.getUserName();
        final Optional<String> password = urlRequest.getPassword();
        if (userName.isPresent() && password.isPresent()) {
            final String fileLinkWithProtocol = String.format("%s://%s:%s@%s", urlRequest.getProtocol().toString(),
                    userName.get(), password.get(), urlRequest.getUrlFilePath());
            try {
                return Optional.of(new URL(fileLinkWithProtocol));
            } catch (final MalformedURLException malformedURLException) {
                log.error("URL : [{}] is not valid, cause: {}.", fileLinkWithProtocol, malformedURLException);
                return Optional.empty();
            }
        } else {
            log.error("Username/Password is not present for protocol: [{}], file: [{}]", urlRequest.getProtocol(),
                    urlRequest.getUrlFilePath());
            return Optional.empty();
        }
    }
}
