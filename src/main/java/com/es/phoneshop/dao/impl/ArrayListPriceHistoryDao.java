package com.es.phoneshop.dao.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.PriceHistoryDao;
import com.es.phoneshop.model.PriceHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ArrayListPriceHistoryDao implements PriceHistoryDao {
    private List<PriceHistory> histories;
    private final AtomicLong productId;
    private final FunctionalReadWriteLock lock;

    private ArrayListPriceHistoryDao() {
        histories = new ArrayList<>();
        productId = new AtomicLong(0);
        lock = new FunctionalReadWriteLock();
    }

    public static ArrayListPriceHistoryDao getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ArrayListPriceHistoryDao INSTANCE = new ArrayListPriceHistoryDao();
    }

    @Override
    public List<PriceHistory> getPriceHistoryOfProduct(Long productId) {
        return lock.read(() -> {
            List<PriceHistory> historyList = histories.stream()
                    .filter(history -> history.getProduct().getId().equals(productId))
                    .collect(Collectors.toList());
            Collections.reverse(historyList);
            return historyList;
        });
    }

    @Override
    public void save(PriceHistory history) {
        lock.write(() -> {
            if (history == null) throw new IllegalArgumentException("History equals null");
            history.setId(productId.incrementAndGet());
            histories.add(history);
        });
    }
}
