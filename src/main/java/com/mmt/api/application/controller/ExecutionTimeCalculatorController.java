package com.mmt.api.application.controller;


import com.mmt.api.domain.entity.RequestData;
import com.mmt.api.domain.service.ExecutionTimeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("/executionTimeCalculator")
public class ExecutionTimeCalculatorController {

    @Autowired
    ExecutionTimeCalculator executionTimeCalculator;

    @PostMapping
    public ResponseEntity<Long> calculate(@RequestBody List<RequestData> requestDataList) {

        final Long totalTime = requestDataList.stream().map(this::execute).reduce(0l, Long::sum);
        System.out.println("Total Time :: "+ totalTime);
        return new ResponseEntity<Long>(totalTime,HttpStatus.OK);
    }

    private long execute(RequestData requestData) {
        try {
            return requestData.isParallel() ? executionTimeCalculator.executeAsParallel(requestData.getUrl(), requestData.getCount()) : executionTimeCalculator.executeAsSequential(requestData.getUrl(), requestData.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
