package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;

import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String sortingField = request.getParameter("sorting");
        String sortingType = request.getParameter("type");
        request.setAttribute("products", productDao.findProducts(description,
                sortingField != null ? SortingField.valueOf(sortingField) : null,
                sortingType != null ? SortingType.valueOf(sortingType) : null));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
