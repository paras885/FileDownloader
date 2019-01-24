package fun.agoda.downloader.factories;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.businesslogics.BusinessLogicForHTTPProtocol;
import fun.agoda.downloader.pojos.Protocol;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(JMockit.class)
public class BusinessLogicFactoryImplTest {

    @Tested
    private BusinessLogicFactoryImpl businessLogicFactory;

    @Injectable
    private Map<Protocol, BusinessLogic> protocolBusinessLogicMap;

    @Mocked
    private BusinessLogicForHTTPProtocol businessLogic;

    @Test
    public void testGetBusinessLogicByProtocol_happyPath_whenBusinessLogicMappingPresentForProtocol() {
        new Expectations() {
            {
                protocolBusinessLogicMap.get(Protocol.HTTP);
                result = businessLogic;
                times = 1;
            }
        };

        final BusinessLogic actualOutput = businessLogicFactory.getBusinessLogicByProtocol(Protocol.HTTP);
        assertThat(actualOutput, instanceOf(BusinessLogicForHTTPProtocol.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBusinessLogicByProtocol_whenBusinessLogicMappingMissingForProtocol_shouldThrowIllegalArgumentException() {
        new Expectations() {
            {
                protocolBusinessLogicMap.get(Protocol.HTTP);
                result = null;
                times = 1;
            }
        };

        businessLogicFactory.getBusinessLogicByProtocol(Protocol.HTTP);
    }
}
