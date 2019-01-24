package fun.agoda.downloader.utils.parsers;

import fun.agoda.downloader.pojos.Protocol;
import fun.agoda.downloader.pojos.UrlRequest;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JMockit.class)
public class UrlParserImplTest {

    private final static String VALID_URL_WIHTOUT_ADDITIONAL_ATTRIBUTES = "ftp://test.rebex.net/readme.txt";
    private final static String VALID_URL_WITH_ADDITIONAL_ATTRIBUTES = "ftp://test.rebex.net/readme.txt/additional-attributes?user=demo&password=password";
    private final static String INVALID_URL_WITHOUT_PROTOCOL_LOCATOR_KEY = "ffafdfsfasdfs:sfasdf:sfas/asdfs//";
    private final static String INVALID_URL_WITHOUT_FILE_PATH = "ftp://";

    @Tested
    private UrlParserImpl urlParser;

    @Test
    public void testParse_whenGivenValidUrl_withoutAdditionalAttributes() {
        final String expectedFilePath = "test.rebex.net/readme.txt";
        final Protocol expectedProtocol = Protocol.FTP;
        final UrlRequest actualUrlRequest = urlParser.parse(VALID_URL_WIHTOUT_ADDITIONAL_ATTRIBUTES);
        assertThat(actualUrlRequest.getUrlFilePath(), is(expectedFilePath));
        assertThat(actualUrlRequest.getProtocol(), is(expectedProtocol));
    }

    @Test
    public void testParse_whenGivenValidUrl_withAdditionalAttributes() {
        final String expectedFilePath = "test.rebex.net/readme.txt";
        final Protocol expectedProtocol = Protocol.FTP;
        final String expectedUserName = "demo";
        final String expectedPassword = "password";
        final UrlRequest actualUrlRequest = urlParser.parse(VALID_URL_WITH_ADDITIONAL_ATTRIBUTES);
        assertThat(actualUrlRequest.getUrlFilePath(), is(expectedFilePath));
        assertThat(actualUrlRequest.getProtocol(), is(expectedProtocol));
        assertThat(actualUrlRequest.getUserName().get(), is(expectedUserName));
        assertThat(actualUrlRequest.getPassword().get(), is(expectedPassword));
    }

    @Test(expected = AssertionError.class)
    public void testParse_whenGivenInvalidUrl_missingProtocolLocatorKey() {
        urlParser.parse(INVALID_URL_WITHOUT_PROTOCOL_LOCATOR_KEY);
    }

    @Test(expected = AssertionError.class)
    public void testParse_whenGivenInvalidUrl_missingFilePath() {
        urlParser.parse(INVALID_URL_WITHOUT_FILE_PATH);
    }
}
