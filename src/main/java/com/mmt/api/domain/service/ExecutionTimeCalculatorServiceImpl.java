package com.mmt.api.domain.service;

import com.mmt.api.domain.entity.APIInvocationResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExecutionTimeCalculatorServiceImpl implements ExecutionTimeCalculator {
    @Override
    public long executeAsParallel(String url, int invocationCount) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(invocationCount);

        final List<Task> tasks = Collections.nCopies(invocationCount, new Task(url));

        long startTime = System.currentTimeMillis();

        final List<Future<APIInvocationResult>> futureResultList = executorService.invokeAll(tasks);

        for (Future<APIInvocationResult> future : futureResultList) {
            System.out.println(future.get());
        }
        executorService.shutdown();
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public long executeAsSequential(String url, int invocationCount) {
        long startTime = System.currentTimeMillis();
        final List<APIInvocationResult> apiInvocationResults = Collections.nCopies(invocationCount, invokeAPI(url));
        apiInvocationResults.forEach(System.out::println);

        return System.currentTimeMillis() - startTime;
    }

    private final class Task implements Callable<APIInvocationResult> {
        private final String url;

        Task(String url) {
            this.url = url;
        }

        @Override
        public APIInvocationResult call() throws Exception {
            return invokeAPI(url);
        }
    }

    private APIInvocationResult invokeAPI(String endPoint) {
        APIInvocationResult apiInvocationResult = null;
        long startTime = System.currentTimeMillis();
        try {
            URL url = new URL(endPoint);
            final URLConnection urlConnection = url.openConnection();
            String contentType = urlConnection.getContentType();
            apiInvocationResult = new APIInvocationResult(endPoint, true, (System.currentTimeMillis() - startTime), contentType);
        } catch (IOException e) {
            e.printStackTrace();
            apiInvocationResult = new APIInvocationResult(endPoint, false, (System.currentTimeMillis() - startTime), null);
        }
        return apiInvocationResult;
    }
}
