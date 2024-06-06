package com.lastvalue.demo;

import com.lastvalue.demo.entity.PriceRecord;
import com.lastvalue.demo.service.PriceService;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication /*implements CommandLineRunner*/ {
//	@Autowired
//	private PriceService priceService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		// Start a new batch run
//		priceService.startBatchRun("1");
//
//		// Upload some dummy price records
//		priceService.uploadRecords("1",Arrays.asList(
//				new PriceRecord("instrument1", LocalDateTime.now().minusHours(1), 100.0),
//				new PriceRecord("instrument1", LocalDateTime.now(), 200.0),
//				new PriceRecord("instrument2", LocalDateTime.now(), 150.0)
//		));
//
//		// Complete the batch run
//		priceService.completeBatchRun("1");
//
//		// Request the last price for "instrument1"
//		priceService.getLastPrice("instrument1").ifPresent(System.out::println);
//
//		// Start another batch run
//		priceService.startBatchRun("1");
//
//		// Upload more dummy price records
//		priceService.uploadRecords("1",Arrays.asList(
//				new PriceRecord("instrument1", LocalDateTime.now().plusHours(1), 250.0)
//		));
//
//		// Complete the batch run
//		priceService.completeBatchRun("1");
//
//		// Request the last price for "instrument1" again
//		priceService.getLastPrice("instrument1").ifPresent(System.out::println);
//	}

}
