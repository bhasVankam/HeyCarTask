package com.heycar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class TransactionHelper {

    @Transactional
    public void inTransaction(Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public <T> T inTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

}
