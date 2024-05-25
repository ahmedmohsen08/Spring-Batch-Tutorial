package com.basata.billnotifications.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiptRepo {
    JdbcTemplate jdbcTemplate;
    public ReceiptRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getReceiptNumber() {
        String sql = "select get_receipt_number()";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
