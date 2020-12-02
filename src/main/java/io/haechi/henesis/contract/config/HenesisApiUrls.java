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
@ConfigurationProperties("henesis-api-urls")
public class HenesisApiUrls {
    private String masterWalletSmartContractCall;
    private String userWalletSmartContractCall;
}
