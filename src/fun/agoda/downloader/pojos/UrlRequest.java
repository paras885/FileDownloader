package fun.agoda.downloader.pojos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Builder
@Data
public class UrlRequest {

    public final static String USERNAME_KEY = "user";
    public final static String PASSWORD_KEY = "password";
    private final static char URL_FILE_SEPARATOR_CHAR = '/';
    private final static char SYSTEM_FILE_SEPARATOR_CHAR = '_';


    private Protocol protocol;
    private String urlFilePath;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Map<String, String> additionalAttributes;

    public Optional<String> getUserName() {
        return getAttributeForKey(USERNAME_KEY);
    }

    public Optional<String> getPassword() {
        return getAttributeForKey(PASSWORD_KEY);
    }

    private Optional<String> getAttributeForKey(final String attributeKey) {
        if (additionalAttributes.containsKey(attributeKey)) {
            return Optional.of(additionalAttributes.get(attributeKey));
        } else {
            return Optional.empty();
        }
    }

    public String getSystemSpecificFileNameFromUrl() {
        return urlFilePath.replace(URL_FILE_SEPARATOR_CHAR, SYSTEM_FILE_SEPARATOR_CHAR);
    }
}
