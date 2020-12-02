package io.haechi.henesis.contract.infra;

import io.haechi.henesis.contract.application.dto.ContractCallRequestBody;
import io.haechi.henesis.contract.config.HenesisApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.ECKeyPair;
import io.haechi.henesis.contract.infra.contracts.IERC20;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Objects;

@Slf4j
@Service
public class EncodeFunction {
    private final IERC20 ierc20;

    public EncodeFunction(HenesisApiProperties henesisApiProperties) {
        String ercTokenAddress = Objects.requireNonNull(henesisApiProperties).getErcTokenAddress();
        ECKeyPair dummyKeyPair = null;
        try {
            dummyKeyPair = Keys.createEcKeyPair();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/e0045a11b5e64a5b97dca82c816a2d1d"));
        this.ierc20 = IERC20.load(
                ercTokenAddress,
                web3j,
                Credentials.create(dummyKeyPair),
                new DefaultGasProvider()
        );
    }

    public String getHexTokenData(ContractCallRequestBody contractCallRequestBody) {
        return this.ierc20.transfer(contractCallRequestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
    }
}
