package com.basata.billnotifications.util;

import com.basata.billnotifications.service.TransactionSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sg.technobiz.client.objects.Transact;

import javax.sql.DataSource;
import java.io.IOException;

@Slf4j
@Component
public class TSUtil {

    @Value("${transaction.server.host}")
    private String host;

    @Value("${transaction.server.port}")
    private int port;

    private DataSource dataSource;

    public TSUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long sendTransaction(Transact tr) {
        log.info("Transaction for TS: {}", tr);
        //0-normal, 1-high

        try {
            long code = sg.technobiz.client.utils.TSUtil.sendTransaction(host, port, dataSource, "", tr);

            log.info("ReceiptID={}, TS response received, ResponseId={}", tr.getReceipt_id(), code);

            return code;
        } catch (IOException e) {
            log.error("IOException, TS not responding", e);
        } catch (Exception e) {
            log.error("Exception", e);
        }

        return -1000L;
    }
}
