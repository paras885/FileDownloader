package fun.agoda.downloader.factories;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.pojos.Protocol;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

public class BusinessLogicFactoryImpl implements BusinessLogicFactory {

    private Map<Protocol, BusinessLogic> protocolBusinessLogicMap;

    @Inject
    public BusinessLogicFactoryImpl(final Map<Protocol, BusinessLogic> protocolBusinessLogicMap) {
        this.protocolBusinessLogicMap = protocolBusinessLogicMap;
    }

    @Override
    public BusinessLogic getBusinessLogicByProtocol(final Protocol protocol) {
        final BusinessLogic businessLogic = protocolBusinessLogicMap.get(protocol);
        if (Objects.isNull(businessLogic)) {
            throw new IllegalArgumentException("No business logic defined for protocol: [" + protocol.toString() + "]");
        }

        return businessLogic;
    }
}
