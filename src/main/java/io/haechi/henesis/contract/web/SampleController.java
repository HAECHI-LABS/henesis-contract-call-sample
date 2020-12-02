package io.haechi.henesis.contract.web;

import io.haechi.henesis.contract.application.dto.ContractCallRequestBody;
import io.haechi.henesis.contract.application.dto.ContractService;
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
    private final ContractService contractService;

    @ApiOperation(value = "Master wallet contract call API", notes = "특정 마스터 지갑에서 일반적인 스마트 컨트랙트 함수를 호출하는 트랜잭션을 발생시킵니다.")
    @PostMapping(value = "/{blockchainSymbol}/{masterWalletId}/contract-call")
    @ResponseStatus(value = HttpStatus.OK)
    public void requestMasterWalletContractCallWithHenesisAPI(
            @PathVariable String blockchainSymbol,
            @PathVariable String masterWalletId,
            @RequestBody @Valid ContractCallRequestBody contractCallRequestBody
    ) {
        this.contractService.requestMasterWalletContractCall(blockchainSymbol, masterWalletId, contractCallRequestBody);
    }

    @ApiOperation(value = "User wallet contract call API", notes = "사용자 지갑에서 일반적인 스마트 컨트랙트 함수를 호출하는 트랜잭션을 발생시킵니다.")
    @PostMapping(value = "/{blockchainSymbol}/{masterWalletId}/{userWalletId}/contract-call")
    @ResponseStatus(value = HttpStatus.OK)
    public void requestUserWalletContractCallWithHenesisAPI(
            @PathVariable String blockchainSymbol,
            @PathVariable String masterWalletId,
            @PathVariable String userWalletId,
            @RequestBody @Valid ContractCallRequestBody contractCallRequestBody
    ) {
        this.contractService.requestUserWalletContractCall(blockchainSymbol, masterWalletId, userWalletId, contractCallRequestBody);
    }
}
