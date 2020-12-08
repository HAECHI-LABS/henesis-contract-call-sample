package io.haechi.henesis.example.config;

import io.haechi.henesis.example.Blockchain;

import java.util.Map;

public class TokenSupplier<T> {
    private final Map<Blockchain, T> t;

    public TokenSupplier(Map<Blockchain, T> map) {
        this.t = map;
    }

    public T supply(Blockchain blockchain) {
        T token = t.get(blockchain);
        if(token == null) {
            throw new IllegalArgumentException(String.format("'%s' is not supported blockchain", blockchain));
        }
        return t.get(blockchain);
    }
}
