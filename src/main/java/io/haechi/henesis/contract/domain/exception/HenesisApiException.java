package io.haechi.henesis.contract.domain.exception;

public class HenesisApiException extends RuntimeException {
    public HenesisApiException(String msg, Throwable t) {
        super(msg, t);
    }

    public HenesisApiException(String msg) {
        super(msg);
    }

    public HenesisApiException() {
        super();
    }
}
