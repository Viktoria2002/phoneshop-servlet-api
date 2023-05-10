package com.es.phoneshop.web;

import com.es.phoneshop.dao.PriceHistoryDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListPriceHistoryDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private PriceHistoryDao priceHistoryDao;

    @Override
    public void init() {
        priceHistoryDao = ArrayListPriceHistoryDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        request.setAttribute("history", priceHistoryDao.getPriceHistoryOfProduct(Long.valueOf(productId.substring(1))));
        request.getRequestDispatcher("/WEB-INF/pages/priceHistory.jsp").forward(request, response);
    }
}
