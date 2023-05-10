package com.es.phoneshop.model.comparator;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Product;

import java.util.Comparator;

public class SearchComparator implements Comparator<Product> {
    private String description;

    public SearchComparator(String description) {
        this.description = description;
    }

    @Override
    public int compare(Product product1, Product product2) {
        if (description == null) return 0;
        return ArrayListProductDao.countFoundWords(description, product2.getDescription())
                - ArrayListProductDao.countFoundWords(description, product1.getDescription());
    }
}
