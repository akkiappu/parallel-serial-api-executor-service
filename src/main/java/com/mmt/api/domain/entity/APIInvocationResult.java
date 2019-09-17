package com.mmt.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIInvocationResult {
    final private String url;
    final private boolean status;
    final private Long executionTime;
    final private String contentType;

    @Override
    public String toString() {
        return "APIInvocationResult:" + (status == true ? "SUCCESS" : "FAILED") + " " + executionTime + " msecs " + "endpoint "+ url + "contentType " +contentType;
    }
}