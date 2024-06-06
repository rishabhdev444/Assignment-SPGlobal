package com.lastvalue.demo.service;

import com.lastvalue.demo.entity.BatchRun;
import com.lastvalue.demo.entity.PriceRecord;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceServiceImpl implements PriceService {
    private Map<String, BatchRun> batchRuns = new ConcurrentHashMap<>();
    private Map<String, PriceRecord> latestPrices = new ConcurrentHashMap<>();

    @Override
    public void startBatchRun(String batchId) {
        if (batchRuns.containsKey(batchId)) {
            throw new IllegalStateException("Batch with the same ID already exists.");
        }
        batchRuns.put(batchId, new BatchRun());
    }

    @Override
    public void uploadRecords(String batchId, List<PriceRecord> records) {
        BatchRun batchRun = batchRuns.get(batchId);
        if (batchRun == null) {
            throw new IllegalStateException("Batch run not started.");
        }
        batchRun.addRecords(records);
    }

    @Override
    public void completeBatchRun(String batchId) {
        BatchRun batchRun = batchRuns.get(batchId);
        if (batchRun == null) {
            throw new IllegalStateException("Batch run not started.");
        }
        batchRun.complete();
        for (PriceRecord record : batchRun.getRecords()) {
            PriceRecord currentRecord = latestPrices.get(record.getId());
            if (currentRecord == null || currentRecord.getAsOf().isBefore(record.getAsOf())) {
                latestPrices.put(record.getId(), record);
            }
        }
        batchRuns.remove(batchId);
    }

    @Override
    public void cancelBatchRun(String batchId) {
        batchRuns.remove(batchId);
    }

    @Override
    public Optional<PriceRecord> getLastPrice(String id) {
        if (!latestPrices.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(latestPrices.get(id));
    }
}


