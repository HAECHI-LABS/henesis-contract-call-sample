package io.haechi.henesis.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Blockchain {
    ETH("eth"),
    KLAY("klay");
    private final String name;
}
