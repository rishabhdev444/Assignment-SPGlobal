package com.lastvalue.demo.entity;

import java.util.ArrayList;
import java.util.List;

public class BatchRun {
    private List<PriceRecord> records = new ArrayList<>();
    private boolean completed = false;

    public void addRecords(List<PriceRecord> newRecords) {
        if (completed) {
            throw new IllegalStateException("Cannot add records to a completed batch.");
        }
        records.addAll(newRecords);
    }

    public void complete() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<PriceRecord> getRecords() {
        return records;
    }
}
