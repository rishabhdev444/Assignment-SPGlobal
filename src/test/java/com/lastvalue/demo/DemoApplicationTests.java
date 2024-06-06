package com.lastvalue.demo;

import com.lastvalue.demo.entity.BatchRun;
import com.lastvalue.demo.entity.PriceRecord;
import com.lastvalue.demo.service.PriceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class DemoApplicationTests {

    @InjectMocks
    private PriceServiceImpl priceService;
    @InjectMocks
    private Map<String, BatchRun> batchRuns = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartBatchRun() {
        String batchId = "batch1";
        BatchRun batchRun=new BatchRun();
        batchRun.addRecords(new ArrayList<>());
        batchRuns.put(batchId,batchRun);
        priceService.startBatchRun(batchId);
        Assertions.assertTrue(batchRuns.containsKey(batchId));
    }

    @Test
    void testStartBatchRunWithExistingBatchId() {
        String batchId = "batch1";
        priceService.startBatchRun(batchId);
        Assertions.assertThrows(IllegalStateException.class, () -> priceService.startBatchRun(batchId));
    }

    @Test
    void testUploadRecords() {
        String batchId = "batch1";
        priceService.startBatchRun(batchId);
        PriceRecord record1 = new PriceRecord("instrument1", LocalDateTime.now(), 100.0);
        PriceRecord record2 = new PriceRecord("instrument2", LocalDateTime.now(), 200.0);
        BatchRun batchRun=new BatchRun();
        batchRun.addRecords(Arrays.asList(record1,record2));
        batchRuns.put(batchId,batchRun);
        priceService.uploadRecords(batchId, Arrays.asList(record1, record2));

        BatchRun batchRun1 = batchRuns.get(batchId);
        Assertions.assertNotNull(batchRun1);
        Assertions.assertEquals(2, batchRun1.getRecords().size());
    }

    @Test
    void testUploadRecordsWithoutStartingBatch() {
        String batchId = "batch1";
        PriceRecord record = new PriceRecord("instrument1", LocalDateTime.now(), 100.0);
        Assertions.assertThrows(IllegalStateException.class, () -> priceService.uploadRecords(batchId, Arrays.asList(record)));
    }

    @Test
    void testCompleteBatchRun() {
        String batchId = "batch1";
        priceService.startBatchRun(batchId);
        PriceRecord record1 = new PriceRecord("instrument1", LocalDateTime.now().minusDays(1), 100.0);
        PriceRecord record2 = new PriceRecord("instrument1", LocalDateTime.now(), 200.0);
        priceService.uploadRecords(batchId, Arrays.asList(record1, record2));
        priceService.completeBatchRun(batchId);

        Assertions.assertFalse(batchRuns.containsKey(batchId));
        Optional<PriceRecord> lastPrice = priceService.getLastPrice("instrument1");
        Assertions.assertTrue(lastPrice.isPresent());
        Assertions.assertEquals(200.0, lastPrice.get().getPayLoad());
    }

    @Test
    void testCompleteBatchRunWithoutStartingBatch() {
        String batchId = "batch1";
        Assertions.assertThrows(IllegalStateException.class, () -> priceService.completeBatchRun(batchId));
    }

    @Test
    void testCancelBatchRun() {
        String batchId = "batch1";
        priceService.startBatchRun(batchId);
        priceService.cancelBatchRun(batchId);
        Assertions.assertFalse(batchRuns.containsKey(batchId));
    }

    @Test
    void testGetLastPrice() {
        String batchId = "batch1";
        priceService.startBatchRun(batchId);
        PriceRecord record = new PriceRecord("instrument1", LocalDateTime.now(), 100.0);
        priceService.uploadRecords(batchId, Arrays.asList(record));
        priceService.completeBatchRun(batchId);

        Optional<PriceRecord> lastPrice = priceService.getLastPrice("instrument1");
        Assertions.assertTrue(lastPrice.isPresent());
        Assertions.assertEquals(100.0, lastPrice.get().getPayLoad());
    }

    @Test
    void testGetLastPriceWithNoRecords() {
        Optional<PriceRecord> lastPrice = priceService.getLastPrice("instrument1");
        Assertions.assertFalse(lastPrice.isPresent());
    }

}
