package com.lastvalue.demo.config;

import com.lastvalue.demo.entity.PriceRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;


    @Bean
    public Job priceBatchJob(ItemReader<PriceRecord> reader,
                             ItemProcessor<PriceRecord, PriceRecord> processor, ItemWriter<PriceRecord> writer,JobRepository jobRepository,PlatformTransactionManager transactionManager) {

        Step step = new StepBuilder("priceBatchStep",jobRepository)
                .<PriceRecord, PriceRecord>chunk(1000,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        return new JobBuilder("priceBatchJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                // Before job logic
                System.out.println("Job started: " + jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                // After job logic
                System.out.println("Job completed: " + jobExecution.getJobInstance().getJobName());
            }
        };
    }

    @Bean
    public ItemReader<PriceRecord> reader() {
        // Sample data for demonstration
        List<PriceRecord> sampleData = List.of(
                new PriceRecord("instrument1", LocalDateTime.now(), 100.0),
                new PriceRecord("instrument2", LocalDateTime.now(), 200.0)
        );
        return new ListItemReader<>(sampleData);
    }

    @Bean
    public ItemProcessor<PriceRecord, PriceRecord> processor() {
        return new ItemProcessor<PriceRecord, PriceRecord>() {
            @Override
            public PriceRecord process(PriceRecord priceRecord) {
                // Processing logic (e.g., validation, transformation)
                return priceRecord;
            }
        };
    }

    @Bean
    public ItemWriter<PriceRecord> writer() {
        return items->items.forEach(item -> System.out.println("Writing item: " + item));
    }


}
