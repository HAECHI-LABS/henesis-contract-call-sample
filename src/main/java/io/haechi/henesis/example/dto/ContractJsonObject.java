package io.haechi.henesis.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Henesis API로부터 받은 응답 오브젝트
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContractJsonObject implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("blockchain")
    private String blockchain;
    @JsonProperty("sender")
    private String sender;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private String status;
    @JsonProperty("fee")
    private String fee;
    @JsonProperty("keyId")
    private String keyId;
    @JsonProperty("isFeeDelegated")
    private Boolean isFeeDelegated;
    @JsonProperty("estimatedFee")
    private String estimatedFee;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("signedMultiSigPayload")
    private SignedMultiSigPayload signedMultiSigPayload;
    @JsonProperty("rawTransaction")
    private RawTransaction rawTransaction;

    public static class SignedMultiSigPayload {
        @JsonProperty("signature")
        private String signature;
        @JsonProperty("multiSigPayload")
        private MultiSigPayload multiSigPayload;
    }

    public static class MultiSigPayload {
        @JsonProperty("value")
        private String value;
        @JsonProperty("walletAddress")
        private String walletAddress;
        @JsonProperty("toAddress")
        private String toAddress;
        @JsonProperty("walletNonce")
        private String walletNonce;
        @JsonProperty("hexData")
        private String hexData;
    }

    public static class RawTransaction {
        @JsonProperty("nonce")
        private String nonce;
        @JsonProperty("to")
        private String to;
        @JsonProperty("value")
        private String value;
        @JsonProperty("data")
        private String data;
        @JsonProperty("gasPrice")
        private String gasPrice;
        @JsonProperty("gasLimit")
        private String gasLimit;
    }
}
