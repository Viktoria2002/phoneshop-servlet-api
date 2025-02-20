package com.es.phoneshop.web;

import com.es.phoneshop.model.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceHistoryPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    private PriceHistoryPageServlet servlet = new PriceHistoryPageServlet();
    private static final String PRODUCT_ID_FROM_URL = "/1";

    @Before
    public void setup() {
        servlet.init();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        servlet.productDao.save(product1);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(PRODUCT_ID_FROM_URL);

        servlet.doGet(request, response);

        verify(request).setAttribute(anyString(), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @After
    public void clear() {
        servlet.productDao.delete(1L);
    }
}
