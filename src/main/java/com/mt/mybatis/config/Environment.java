package com.mt.mybatis.config;

import com.mt.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

public class Environment {
    private final String id;
    private final TransactionFactory transactionFactory;
    private final DataSource dataSource;

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }
}
