package com.lastvalue.demo.service;

import com.lastvalue.demo.entity.PriceRecord;

import java.util.List;
import java.util.Optional;

public interface PriceService {

        void startBatchRun(String batchId);
        void uploadRecords(String batchId, List<PriceRecord> records);
        void completeBatchRun(String batchId);
        void cancelBatchRun(String batchId);
        Optional<PriceRecord> getLastPrice(String instrumentId);


}
