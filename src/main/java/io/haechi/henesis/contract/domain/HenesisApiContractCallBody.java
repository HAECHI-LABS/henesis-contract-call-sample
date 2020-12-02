package io.haechi.henesis.contract.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HenesisApiContractCallBody {
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
}
