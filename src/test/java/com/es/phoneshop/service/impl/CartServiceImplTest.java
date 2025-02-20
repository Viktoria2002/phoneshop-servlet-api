package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {
    @InjectMocks
    private CartServiceImpl cartServiceImpl;
    @Mock
    private ProductDao productDao;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    private Cart cart;
    private static final Long NOT_EXISTING_IN_CART_ID = 4L;
    private static final Long EXISTING_IN_CART_ID = 3L;
    private static final int QUANTITY_MORE_THAN_STOCK = 40;
    private static final int QUANTITY_LESS_THAN_STOCK = 25;
    private static final int CART_ITEM_INDEX = 2;
    private static final int QUANTITY = 1;
    private static final int NEW_QUANTITY = 5;

    @Before
    public void setup() {
        cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"), 1));
        cartItems.add(new CartItem(new Product(2L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"), 2));
        cartItems.add(new CartItem(new Product(3L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"), 10));
        cart.setCartItems(cartItems);
    }
    @Test
    public void testGetCartWhenCartNotExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        cartServiceImpl.getCart(request);

        verify(session).setAttribute(anyString(), any());
    }

    @Test
    public void testGetCartWhenCartExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(cart);

        cartServiceImpl.getCart(request);

        verify(session, times(0)).setAttribute(anyString(), any());
    }

    @Test
    public void testAddNewProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(4L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartServiceImpl.add(NOT_EXISTING_IN_CART_ID, QUANTITY, cart);

        int expectedCountOfCartItems = 4;
        assertEquals(expectedCountOfCartItems, cart.getCartItems().size());
    }

    @Test
    public void testAddExistingProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(3L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartServiceImpl.add(EXISTING_IN_CART_ID, QUANTITY, cart);

        int expectedQuantity = 11;
        assertEquals(expectedQuantity, cart.getCartItems().get(CART_ITEM_INDEX).getQuantity());
    }

    @Test
    public void testUpdateProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(3L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartServiceImpl.update(EXISTING_IN_CART_ID, NEW_QUANTITY, cart);

        assertEquals(NEW_QUANTITY, cart.getCartItems().get(CART_ITEM_INDEX).getQuantity());
    }

    @Test
    public void testDeleteProduct() {
        cartServiceImpl.delete(EXISTING_IN_CART_ID, cart);

        assertFalse(cart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(EXISTING_IN_CART_ID)));
    }

    @Test(expected = OutOfStockException.class)
    public void testExceptionForStock() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(3L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartServiceImpl.add(EXISTING_IN_CART_ID, QUANTITY_MORE_THAN_STOCK, cart);
    }

    @Test(expected = OutOfStockException.class)
    public void testExceptionForProductQuantity() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(3L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartServiceImpl.add(EXISTING_IN_CART_ID, QUANTITY_LESS_THAN_STOCK, cart);
    }
}
