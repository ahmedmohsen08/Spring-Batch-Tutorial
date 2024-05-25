package com.basata.billnotifications.batch.reader;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;

import java.util.List;

public class ExecutionContextItemReader<T> implements ItemReader<T> {

    private final String key;
    private int currentIndex = 0;

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public ExecutionContextItemReader(String key) {
        this.key = key;
    }

    @Override
    public T read() throws Exception {
        List<T> items = (List<T>) stepExecution.getJobExecution().getExecutionContext().get(key);
        if (items == null) {
            return null;
        }

        if (currentIndex < items.size()) {
            return items.get(currentIndex++);
        } else {
            return null;
        }
    }
}
