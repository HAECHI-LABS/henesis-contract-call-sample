package io.haechi.henesis.contract.application.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ContractCallRequestBody {
    @NotEmpty
    private String toAddress;
    @NotEmpty
    private String value;
    private String gasPrice;
    private String gasLimit;
}
