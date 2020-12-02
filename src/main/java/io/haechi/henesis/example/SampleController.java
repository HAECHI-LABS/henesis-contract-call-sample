package io.haechi.henesis.example;

import io.haechi.henesis.example.dto.ContractCallRequestBody;
import io.haechi.henesis.example.dto.ContractJsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"1. SampleController"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class SampleController {
    private final ContractCallService contractCallService;

    @ApiOperation(value = "ERC20Trasfer API", notes = "마스터 지갑에서 ERC20 컨트랙트 함수를 호출하는 트랜잭션을 발생시킵니다.")
    @PostMapping(value = "/eth/{masterWalletId}/contract-call")
    @ResponseStatus(value = HttpStatus.OK)
    public ContractJsonObject ERC20Trasfer(
            @PathVariable String masterWalletId,
            @RequestBody @Valid ContractCallRequestBody contractCallRequestBody
    ) {
        log.info("ERC20 token contract call has been requested.");
        return this.contractCallService.contractCall(masterWalletId, contractCallRequestBody);
    }
}
