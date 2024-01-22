package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.model.CartItem;
import com.commenau.model.User;
import com.commenau.service.CartService;
import com.commenau.util.HttpUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/carts")
public class CartController extends HttpServlet {
    @Inject
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));
        if (user != null) {
            List<CartItemDTO> items = cartService.getCartByUserId(user.getId());
            if (items.isEmpty()) {
                request.getRequestDispatcher("/customer/empty-cart.jsp").forward(request, response);
            } else {
                double totalPrice = cartService.totalPrice(items);
                request.setAttribute("cart", items);
                request.setAttribute("totalPrice", totalPrice);
                request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
            }
        } else {
            //get products from cookie
            List<CartItemDTO> items = cartService.getItemFromCookies(request.getCookies());
            if (items.isEmpty())
                request.getRequestDispatcher("/customer/empty-cart.jsp").forward(request, response);
            else {
                double totalPrice = cartService.totalPrice(items);
                request.setAttribute("cart", items);
                request.setAttribute("totalPrice", totalPrice);
                request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the logged-in user
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));

        // Map incoming JSON data to CartItem
        CartItem cartItem = HttpUtil.of(request.getReader()).toModel(CartItem.class);

        if (cartItem == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // Ensure quantity is at least 1
        if (cartItem.getQuantity() == 0) {
            cartItem.setQuantity(1);
        }

        // Handle logic based on user authentication
        if (user != null) {
            // Use database for authenticated users
            boolean result = cartService.addProductToCart(user.getId(), cartItem.getProductId(), cartItem.getQuantity());
            if (result)
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            // Use cookie for non-authenticated users
            Cookie[] cookies = request.getCookies();
            Cookie cookie = cartService.addItemToCookie(cookies, cartItem);
            if (cookie != null) {
                response.addCookie(cookie);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Map incoming JSON data to a Map
        Map<String, String> map = HttpUtil.of(request.getReader()).toModel(Map.class);

        // Get the logged-in user
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));

        // Handle logic based on user authentication
        if (user != null) {
            // Use database for authenticated users to update the cart
            boolean result = cartService.updateCart(map, user.getId());
            if (result)
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            // Use cookie for non-authenticated users to update the cart
            Cookie[] cookies = request.getCookies();
            List<Cookie> cookieList = cartService.updateItemInCookies(cookies, map);
            if (!cookieList.isEmpty()) {
                cookieList.forEach(response::addCookie);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = ((User) request.getSession(false).getAttribute(SystemConstant.AUTH));
        Integer productId = HttpUtil.of(request.getReader()).toModel(Integer.class);
        //remove in database
        if (user != null) {
            boolean result = false;
            // delete a product
            if (productId != null && productId > 0) {
                result = cartService.deleteProduct(productId, user.getId());
            } else {
                //delete all product in cart
                result = cartService.deleteAll(user.getId());
            }
            response.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            //remove in cookies
            cartService.removeItemsInCookies(request.getCookies(), productId).forEach(response::addCookie);
            response.setStatus(HttpServletResponse.SC_OK);

        }

    }

}
