package io.haechi.henesis.example.config;

import io.haechi.henesis.example.contracts.IERC20;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@Slf4j
@Configuration
public class Config {
    @Autowired
    private HenesisApiProperties henesisApiProperties;

    private Web3j web3j() {
        return Web3j.build(new HttpService(this.henesisApiProperties.getWeb3Host()));
    }

    /**
     * IERC20 wrapper 코드를 빈으로 등록합니다.
     * IERC20을 load하여 각 function 들을 사용할 수 있습니다. 각 파라미터는 IERC20.java 에서 확인할 수 있습니다.
     * load param1 - application.yaml에 입력한 ercTokenAddress
     * load param2 - Web3J
     * load param3 - 만들어둔 키페어(privateKey, publicKey)로 Credentials를 생성합니다.
     * load param4 - gasProvider를 사용하거나, 또는 gasPrice와 gasLimit을 입력합니다.
     */
    @Bean
    public IERC20 ierc20() {
        try {
            ECKeyPair dummyKeyPair = Keys.createEcKeyPair();
            return IERC20.load(
                    this.henesisApiProperties.getErcTokenAddress(),
                    this.web3j(),
                    Credentials.create(dummyKeyPair),
                    new DefaultGasProvider()
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Bean
    @Qualifier("passphrase")
    public String passphrase() {
        return this.henesisApiProperties.getPassphrase();
    }
}
