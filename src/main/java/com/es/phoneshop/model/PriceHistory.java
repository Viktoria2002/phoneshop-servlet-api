package com.es.phoneshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceHistory {
    private Long id;
    private LocalDate startDate;
    private BigDecimal price;
    private Currency currency;
    private Product product;

    public PriceHistory() {
    }

    public PriceHistory(LocalDate startDate, BigDecimal price, Currency currency, Product product) {
        this.startDate = startDate;
        this.price = price;
        this.currency = currency;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
