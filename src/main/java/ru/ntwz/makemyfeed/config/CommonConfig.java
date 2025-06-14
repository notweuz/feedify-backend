package ru.ntwz.makemyfeed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("common")
public class CommonConfig {
    private String version;
    private String env;
    private String publicDomain;
    private Content content;

    @Data
    public static class Content {
        private String storage;
        private long maxSize;
        private int maxAttachments;
    }
}
