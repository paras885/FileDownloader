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
public class BusinessLogicForHTTPProtocol extends AbstractBusinessLogic {

    @Inject
    public BusinessLogicForHTTPProtocol(final FileManipulator fileManipulator,
                                        final UrlToFileTransferrer channelToFileTransferrer) {
        super.fileManipulator = fileManipulator;
        super.channelToFileTransferrer = channelToFileTransferrer;
    }

    @Override
    protected Optional<URL> extractUrlFromRequest(final UrlRequest urlRequest) {
        final String fileLinkWithProtocol = String.format("%s://%s", urlRequest.getProtocol().toString(),
                urlRequest.getUrlFilePath());
        try {
            return Optional.of(createURLFromLink(fileLinkWithProtocol));
        } catch (final MalformedURLException malformedURLException) {
            log.error("URL : [{}] is not valid, cause: {}.", fileLinkWithProtocol, malformedURLException);
            return Optional.empty();
        }
    }

    private URL createURLFromLink(final String urlLink) throws MalformedURLException {
        return new URL(urlLink);
    }
}
