package io.haechi.henesis.contract.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    private boolean success;
    private int code;
    private String msg;
}
