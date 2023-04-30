package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProducts() {
        List<Product> products = productDao.findProducts();
        assertTrue(products.stream().allMatch(product -> product.getPrice() != null &&
                product.getStock() > 0));
    }
    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() {
        Product result = productDao.getProduct(2L);
        assertEquals("sgs2", result.getCode());
    }

    @Test
    public void testGetProductNotNull() {
        Product result = productDao.getProduct(2L);
        assertNotNull(result);
    }

    @Test
    public void testSave() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        assertEquals(product, productDao.getProduct(product.getId()));
    }

    @Test
    public void testUpdate() {
        Currency usd = Currency.getInstance("USD");
        List<Product> products = productDao.findProducts();
        Product product = new Product(1L, "sgs", "Apple iPhone 6", new BigDecimal(1200), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        int productIndex = 0;
        products.set(productIndex, product);
        assertEquals(products.get(productIndex), product);
    }

    @Test
    public void testDelete() {
        int size = productDao.findProducts().size();
        productDao.delete(1L);
        assertEquals(size - 1, productDao.findProducts().size());
    }
}
