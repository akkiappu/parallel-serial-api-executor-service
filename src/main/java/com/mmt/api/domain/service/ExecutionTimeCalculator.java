package com.mmt.api.domain.service;

public interface ExecutionTimeCalculator {

    long executeAsParallel(String url, int invocationCount) throws Exception;
    long executeAsSequential(String url, int invocationCount) throws Exception;
}
