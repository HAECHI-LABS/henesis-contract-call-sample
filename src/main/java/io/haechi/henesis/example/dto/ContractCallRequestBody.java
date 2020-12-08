package io.haechi.henesis.example.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * SampleController에서 입력받을 body parameter
 */

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
