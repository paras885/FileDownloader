package fun.agoda.downloader.concurrent;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.pojos.UrlRequest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class BusinessLogicRunnableTest {

    @Tested
    private BusinessLogicRunnable businessLogicRunnable;

    @Injectable private BusinessLogic businessLogic;
    @Injectable private UrlRequest urlRequest;
    @Injectable private String protocolDirectoryPath;

    @Test
    public void testRun_happyPath_applyBusinessLogicSuccessfully() {
        new Expectations() {
            {
                businessLogic.apply(urlRequest, protocolDirectoryPath);
                times = 1;
            }
        };

        businessLogicRunnable.run();
    }

    @Test
    public void testRun_whenBusinessLogicFail_shouldNotThrowAnyExceptionToContinueOtherTasks() {
        new Expectations() {
            {
                businessLogic.apply(urlRequest, protocolDirectoryPath);
                result = new Exception();
                times = 1;
            }
        };

        businessLogicRunnable.run();
    }
}
