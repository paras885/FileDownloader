package fun.agoda.downloader.bindingmodules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import fun.agoda.downloader.businesslogics.BusinessLogic;
import fun.agoda.downloader.businesslogics.BusinessLogicForFTPProtocol;
import fun.agoda.downloader.businesslogics.BusinessLogicForHTTPProtocol;
import fun.agoda.downloader.factories.BusinessLogicFactory;
import fun.agoda.downloader.factories.BusinessLogicFactoryImpl;
import fun.agoda.downloader.pojos.Protocol;

public class BusinessLogicFactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BusinessLogicFactory.class).to(BusinessLogicFactoryImpl.class);
        MapBinder<Protocol, BusinessLogic> mapBinder = MapBinder.newMapBinder(binder(), Protocol.class,
                BusinessLogic.class);
        mapBinder.addBinding(Protocol.HTTP).to(BusinessLogicForHTTPProtocol.class);
        mapBinder.addBinding(Protocol.HTTPS).to(BusinessLogicForHTTPProtocol.class);
        mapBinder.addBinding(Protocol.FTP).to(BusinessLogicForFTPProtocol.class);
    }
}
