package io.haechi.henesis.contract.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@PropertySource(value = "classpath:/application.yaml")
@ConfigurationProperties("henesis")
public class HenesisApiProperties {
    private String apiSecret;
    private String accessToken;
    private String passphrase;
    private String baseUrl;
    private String ercTokenAddress;
}
