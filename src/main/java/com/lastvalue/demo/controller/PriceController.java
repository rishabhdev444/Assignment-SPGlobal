package com.lastvalue.demo.controller;

import com.lastvalue.demo.entity.PriceRecord;
import com.lastvalue.demo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

//  EndPoint Url - http://localhost:9191/api/prices/batch/1/start
    @PostMapping("/batch/{batchId}/start")
    public void startBatch(@PathVariable String batchId) {
        priceService.startBatchRun(batchId);
    }

//  EndPoint Url - http://localhost:9191/api/prices/batch/1/upload
    @PostMapping("/batch/{batchId}/upload")
    public void uploadRecords(@PathVariable String batchId, @RequestBody List<PriceRecord> records) {
        priceService.uploadRecords(batchId, records);
    }

//  EndPoint Url - http://localhost:9191/api/prices/batch/1/complete
    @PostMapping("/batch/{batchId}/complete")
    public void completeBatch(@PathVariable String batchId) {
        priceService.completeBatchRun(batchId);
    }


//  EndPoint Url - http://localhost:9191/api/prices/batch/1/
    @DeleteMapping("/batch/{batchId}")
    public void cancelBatch(@PathVariable String batchId) {
        priceService.cancelBatchRun(batchId);
    }


//  EndPoint Url - http://localhost:9191/api/prices/S001
    @GetMapping("/{id}")
    public Optional<PriceRecord> getLastPrice(@PathVariable String id) {
        return priceService.getLastPrice(id);
    }
}
