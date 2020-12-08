package io.haechi.henesis.example;

import io.haechi.henesis.example.dto.ContractJsonObject;
import io.haechi.henesis.example.dto.HenesisApiContractCallBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnclaveHttpClient {
    private final RestTemplate restTemplate;

    /**
     * Henesis API를 resttemplate을 활용하여 호출합니다.
     * Path Variable은 Map 형식으로, Body Parameters는 buildData()로 만든 객체를 입력합니다.
     */
    public ContractJsonObject sendContractCall(Blockchain blockchain, String masterWalletId, HenesisApiContractCallBody data) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("blockchainSymbol", blockchain.getName());
            params.put("masterWalletId", masterWalletId);
            return this.restTemplate.exchange(
                    "/{blockchainSymbol}/wallets/{masterWalletId}/contract-call",
                    HttpMethod.POST,
                    new HttpEntity<>(data),
                    ContractJsonObject.class,
                    params
            ).getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
