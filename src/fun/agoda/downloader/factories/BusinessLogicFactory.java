package fun.agoda.downloader.factories;

import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.pojos.Protocol;

@FunctionalInterface
public interface BusinessLogicFactory {

    BusinessLogic getBusinessLogicByProtocol(final Protocol protocol);
}
