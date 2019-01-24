package fun.agoda.downloader.utils.parsers;

import fun.agoda.downloader.pojos.Protocol;
import fun.agoda.downloader.pojos.UrlRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  urls will follow this pattern :
 *  protocol://filepath/additional-attributes?username=SAMPLE&password=SAMPLE
 */
@Slf4j
public class UrlParserImpl implements UrlParser {

    private final static String PROTOCOL_LOCATOR_KEY = "://";
    private final static String ADDITIONAL_ATTRIBUTES_KEY = "/additional-attributes\\?";
    private final static String ADDITIONAL_ATTRIBUTES_SEPARATOR = "&";
    private final static String ADDITIONAL_ATTRIBUTES_VALUE_SEPARATOR = "=";

    @Override
    public UrlRequest parse(final String url) {
        final String[] tokenizedUrlByProtocolLocatorKey = url.split(PROTOCOL_LOCATOR_KEY);
        assert tokenizedUrlByProtocolLocatorKey.length == 2;
        final Protocol protocol = Protocol.getProtocolByName(tokenizedUrlByProtocolLocatorKey[0]);

        final String[] tokenizedUrlByAdditionalAttributes = tokenizedUrlByProtocolLocatorKey[1]
                .split(ADDITIONAL_ATTRIBUTES_KEY);
        assert tokenizedUrlByAdditionalAttributes.length >= 1;

        final String filePath = tokenizedUrlByAdditionalAttributes[0];
        final Map<String, String> additionalAttributes = tokenizedUrlByAdditionalAttributes.length > 1
                ? extractadditionalAttributes(tokenizedUrlByAdditionalAttributes[1])
                : null;
        log.info("Parsed tokens from url protocol: [{}], filePath: [{}], additionalAttributes: [{}]", protocol,
                filePath, additionalAttributes);

        return UrlRequest.builder()
                .protocol(protocol)
                .urlFilePath(filePath)
                .additionalAttributes(additionalAttributes)
                .build();
    }

    private Map<String, String> extractadditionalAttributes(final String requestBody) {
        return Arrays.stream(requestBody.split(ADDITIONAL_ATTRIBUTES_SEPARATOR))
                .map(additionalAttribute -> additionalAttribute.split(ADDITIONAL_ATTRIBUTES_VALUE_SEPARATOR))
                .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1]));
    }
}
