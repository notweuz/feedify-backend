package ru.ntwz.makemyfeed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JWTConfig {
    private String secret;

    private long expiration;
}
