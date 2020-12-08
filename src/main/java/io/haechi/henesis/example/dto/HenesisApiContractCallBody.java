package io.haechi.henesis.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Henesis API에서 입력받을 body parameter
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HenesisApiContractCallBody {
    private static HenesisApiContractCallBody henesisApiContractCallBody;
    @JsonProperty("contract_address")
    private String contractAddress;
    @JsonProperty("value")
    private String value;
    @JsonProperty("data")
    private String data;
    @JsonProperty("passphrase")
    private String passphrase;
    @JsonProperty("gas_price")
    private String gasPrice;
    @JsonProperty(value = "gas_limit")
    private String gasLimit;

    public HenesisApiContractCallBody(String contractAddress, String value, String data, String passphrase, String gasLimit, String gasPrice) {
        this.contractAddress = contractAddress;
        this.value = value;
        this.data = data;
        this.passphrase = passphrase;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
    }

    public static HenesisApiContractCallBody of(String contractAddress, String data, String passphrase, String gasLimit, String gasPrice) {
        return new HenesisApiContractCallBody(contractAddress, "0x0", data, passphrase, gasLimit, gasPrice);
    }
}
