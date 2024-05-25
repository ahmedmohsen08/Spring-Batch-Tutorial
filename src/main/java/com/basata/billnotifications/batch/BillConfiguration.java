package com.basata.billnotifications.batch;

import com.basata.billnotifications.batch.processor.SendInquiryProcessor;
import com.basata.billnotifications.batch.writer.BillItemWriter;
import com.basata.billnotifications.model.BillingData;
import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.service.TransactionSequenceService;
import com.basata.billnotifications.util.TSUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BillConfiguration {

    @Bean
    public Job billJob(JobRepository jobRepository, Step fetchData) {
        return new JobBuilder("BillingJob", jobRepository)
                .start(fetchData)
                .build();
    }

    @Bean
    public Step fetchData(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          ItemReader<BillingData> billingDataTableReader,
                          ItemProcessor<BillingData, ProcessedBillingData> sendInquiryProcessor,
                          ItemWriter<ProcessedBillingData> itemWriter) {
        return new StepBuilder("fetchData", jobRepository)
                .<BillingData, ProcessedBillingData>chunk(100, transactionManager)
                .reader(billingDataTableReader)
                .processor(sendInquiryProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BillingData> billingDataTableReader(DataSource dataSource) {
        String sql = """
            select t.id AS transaction_id,
                   t.from_account AS user_account,
                   t.to_account AS service_account,
                   t.user_id AS customer_id,
                   t.receipt_id,
                   t.request,
                   t.client_id
            from transact t
            where id='1737872976'
            """;
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billingDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    public ItemProcessor<BillingData, ProcessedBillingData> sendInquiryProcessor(TSUtil tsUtil, TransactionSequenceService transactionSequenceService, ObjectMapper mapper) {
        return new SendInquiryProcessor(tsUtil, transactionSequenceService, mapper);
    }

//    @Bean
//    public JdbcTransactionManager transactionManager(DataSource dataSource) {
//        return new JdbcTransactionManager(dataSource);
//    }

    @Bean
    public ItemWriter<ProcessedBillingData> billItemWriter(DataSource dataSource) {
        return new BillItemWriter();
    }
}
