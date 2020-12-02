package io.haechi.henesis.contract.infra;

import io.haechi.henesis.contract.config.HenesisApiProperties;
import io.haechi.henesis.contract.config.HenesisApiUrls;
import io.haechi.henesis.contract.domain.HenesisApiClient;
import io.haechi.henesis.contract.domain.HenesisApiContractCallBody;
import io.haechi.henesis.contract.domain.exception.HenesisApiException;
import io.haechi.henesis.contract.infra.dto.SmartContractJsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpHenesisApiClient implements HenesisApiClient {
    private final HenesisApiProperties henesisApiProperties;
    private final HenesisApiUrls henesisApiUrls;
    private final RestTemplate restTemplate;

    public SmartContractJsonObject masterWalletContractCallHenesisApi(
            String blockchainSymbol,
            String masterWalletId,
            HenesisApiContractCallBody henesisApiContractCallBody
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("blockchain_symbol", blockchainSymbol);
            params.put("master_wallet_id", masterWalletId);
            String url = this.henesisApiUrls.getMasterWalletSmartContractCall();
            return this.restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity(henesisApiContractCallBody),
                    SmartContractJsonObject.class,
                    params
            ).getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HenesisApiException(e.getMessage());
        }
    }

    public SmartContractJsonObject userWalletContractCallHenesisApi(
            String blockchainSymbol,
            String masterWalletId,
            String userWalletId,
            HenesisApiContractCallBody henesisApiContractCallBody
    ) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("blockchain_symbol", blockchainSymbol);
            params.put("master_wallet_id", masterWalletId);
            params.put("user_wallet_id", userWalletId);
            String url = this.henesisApiUrls.getUserWalletSmartContractCall();
            return this.restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity(henesisApiContractCallBody),
                    SmartContractJsonObject.class,
                    params
            ).getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new HenesisApiException(e.getMessage());
        }
    }
}
