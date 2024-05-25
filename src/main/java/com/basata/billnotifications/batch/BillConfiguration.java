package com.basata.billnotifications.batch;

import com.basata.billnotifications.batch.processor.SendInquiryProcessor;
import com.basata.billnotifications.batch.reader.ExecutionContextItemReader;
import com.basata.billnotifications.batch.tasklet.WaitTransactionUpdateTasklet;
import com.basata.billnotifications.batch.writer.BillItemWriter;
import com.basata.billnotifications.model.BillingData;
import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.service.TransactionSequenceService;
import com.basata.billnotifications.util.TSUtil;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class BillConfiguration {

    @Bean
    public Job billJob(JobRepository jobRepository, Step fetchData, Step getInquiryResponse, Step waitForTransactionUpdate) {
        return new JobBuilder("BillingJob", jobRepository)
                .start(fetchData)
                .next(waitForTransactionUpdate)
                .next(getInquiryResponse)
                .build();
    }

    @Bean
    public Step fetchData(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          ItemReader<BillingData> billingDataTableReader,
                          ItemProcessor<BillingData, ProcessedBillingData> sendInquiryProcessor,
                          ListItemWriter<ProcessedBillingData> listItemWriter) {
        return new StepBuilder("fetchData", jobRepository)
                .<BillingData, ProcessedBillingData>chunk(100, transactionManager)
                .reader(billingDataTableReader)
                .processor(sendInquiryProcessor)
                .writer(listItemWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        JobExecution jobExecution = stepExecution.getJobExecution();
                        ExecutionContext jobContext = jobExecution.getExecutionContext();
                        jobContext.put("billingData", listItemWriter.getWrittenItems());
                        return ExitStatus.COMPLETED;
                    }
                })
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    public Step waitForTransactionUpdate(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("waitForTransactionUpdate", jobRepository)
                .tasklet(new WaitTransactionUpdateTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step getInquiryResponse(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   ItemReader<ProcessedBillingData> listItemReader,
                                   ItemProcessor<ProcessedBillingData, ProcessedBillingData> compositeProcessor,
                                   ItemWriter<ProcessedBillingData> billItemWriter) {
        return new StepBuilder("getInquiryResponse", jobRepository)
                .<ProcessedBillingData, ProcessedBillingData>chunk(100, transactionManager)
                .reader(listItemReader)
                .processor(compositeProcessor)
                .writer(billItemWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BillingData> billingDataTableReader(DataSource dataSource) {
        String sql = "select * from bills_for_notifications_v";
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billingDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    public ItemProcessor<BillingData, ProcessedBillingData> sendInquiryProcessor(TSUtil tsUtil, TransactionSequenceService transactionSequenceService) {
        return new SendInquiryProcessor(tsUtil, transactionSequenceService);
    }

    @Bean
    public ItemWriter<ProcessedBillingData> billItemWriter() {
        return new BillItemWriter();
    }

    @Bean
    public ListItemWriter<ProcessedBillingData> listItemWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    public ItemReader<ProcessedBillingData> listItemReader() {
        return new ExecutionContextItemReader<>("billingData");
    }

    @Bean
    public CompositeItemProcessor<ProcessedBillingData, ProcessedBillingData> compositeProcessor(ItemProcessor<ProcessedBillingData, ProcessedBillingData> sendNotificationProcessor,
                                                                                                 ItemProcessor<ProcessedBillingData, ProcessedBillingData> getInquiryResponseProcessor) {
        CompositeItemProcessor<ProcessedBillingData, ProcessedBillingData> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(getInquiryResponseProcessor, sendNotificationProcessor));
        return processor;
    }
}
