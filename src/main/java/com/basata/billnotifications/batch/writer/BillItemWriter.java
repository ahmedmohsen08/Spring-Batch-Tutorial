package com.basata.billnotifications.batch.writer;

import com.basata.billnotifications.model.ProcessedBillingData;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class BillItemWriter implements ItemWriter<ProcessedBillingData> {

    @Override
    public void write(Chunk<? extends ProcessedBillingData> chunk) throws Exception {

    }
}
