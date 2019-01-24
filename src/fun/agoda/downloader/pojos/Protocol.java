package fun.agoda.downloader.pojos;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Protocol {

    HTTP("http"),
    HTTPS("https"),
    FTP("ftp");

    private final static Map<String, Protocol> STRING_TO_ENUM_MAP;
    private final String protocolName;

    Protocol(final String protocolName) {
        this.protocolName = protocolName;
    }

    public String toString() {
        return this.protocolName;
    }

    public static Protocol getProtocolByName(String protocolName) {
        Protocol protocol = STRING_TO_ENUM_MAP.get(protocolName.toLowerCase());
        if (Objects.isNull(protocol)) {
            throw new IllegalArgumentException("Not valid protocolName : [" + protocolName + "].");
        }

        return protocol;
    }

    static {
        STRING_TO_ENUM_MAP = Arrays.stream(Protocol.values())
                .collect(Collectors.toMap(Protocol::toString, protocol -> protocol));
    }
}
