package com.es.phoneshop.web;

import com.es.phoneshop.dao.PriceHistoryDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListPriceHistoryDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.PriceHistory;
import com.es.phoneshop.model.Product;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ProductDemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;
    private PriceHistoryDao priceHistoryDao;

    public ProductDemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
        this.priceHistoryDao = ArrayListPriceHistoryDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean demoData = Boolean.valueOf(sce.getServletContext().getInitParameter("demoData"));
        if (demoData) {
            getSampleProducts().stream()
                    .forEach(product -> productDao.save(product));
            getSampleProductsPriceHistory().stream().forEach(priceHistory -> priceHistoryDao.save(priceHistory));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    public List<Product> getSampleProducts() {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        result.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        result.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        result.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        result.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        result.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        result.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        result.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        result.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        result.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        result.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        result.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        result.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        result.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        return result;
    }

    public List<PriceHistory> getSampleProductsPriceHistory() {
        Currency usd = Currency.getInstance("USD");
        List<PriceHistory> histories = new ArrayList<>();
        List<Product> products = productDao.findProducts(null, null, null);
        for (Product product : products) {
            LocalDate startDate = LocalDate.now();
            List<PriceHistory> history = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                history.add(new PriceHistory(startDate.minusMonths(1 + (int) (Math.random() * 15)),
                        new BigDecimal(200 + (int) (Math.random() * 300)), usd, product));
            }
            Collections.sort(history, Comparator.comparing(PriceHistory::getStartDate));
            history.get(2).setPrice(product.getPrice());
            histories.addAll(history);
        }
        return histories;
    }
}
