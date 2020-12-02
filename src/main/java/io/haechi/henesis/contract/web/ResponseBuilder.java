package io.haechi.henesis.contract.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ResponseBuilder {
    public CommonResult getSuccessResult(String msg) {
        CommonResult result = new CommonResult();
        result.setMsg(msg);
        setSuccessResult(result);
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
    }

    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    @Getter
    @AllArgsConstructor
    public enum CommonResponse {
        SUCCESS(0, "성공"),
        FAIL(-1, "실패");

        int code;
        String msg;
    }
}
