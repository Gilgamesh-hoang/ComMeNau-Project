package com.commenau.service;

import com.commenau.dao.CartDAO;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.ProductDTO;
import com.commenau.model.Cart;
import com.commenau.model.CartItem;
import com.commenau.util.RoundUtil;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.*;
import java.util.stream.Collectors;

public class CartService {
    @Inject
    private CartDAO cartDAO;
    @Inject
    private ProductService productService;

    public List<CartItemDTO> getCartByUserId(long userId) {
        List<CartItemDTO> result = new ArrayList<>();
        cartDAO.findCartItemByUserId(userId).forEach(item -> {
            ProductDTO product = productService.getByIdWithAvatar(item.getProductId());
            CartItemDTO itemDTO = CartItemDTO.builder().id(item.getId())
                    .product(product).quantity(item.getQuantity()).build();
            result.add(itemDTO);
        });
        return result;
    }

    public long totalPrice(List<CartItemDTO> items) {
        long total = 0;
        for (CartItemDTO item : items) {
            double price = item.getProduct().getPrice();
            float discount = item.getProduct().getDiscount();
            total += RoundUtil.roundPrice((price * (1 - discount))) * item.getQuantity();
        }
        return total;
    }

    public boolean addProductToCart(long userId, int productId, int quantity) {
        // If the product is out of stock or the product is out of business
        if (!productService.checkProductValid(productId, quantity))
            return false;

        Cart cart = cartDAO.findCartByUserId(userId);
        //if user doesn't have cart, this step is only performed once when the user add the first product to the cart
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart = cartDAO.save(cart);
        }
        CartItem itemEntity = cartDAO.findCartItemByProductAndCart(productId, cart.getId());
        // if product isn't exists in cart then create a new item
        if (itemEntity == null) {
            itemEntity = new CartItem();
            itemEntity.setProductId(productId);
            quantity = productService.checkAvailable(productId, quantity);
            itemEntity.setQuantity(quantity);
            itemEntity.setCartId(cart.getId());
            return cartDAO.saveCartItem(itemEntity);
        } else {
            //if it already has then update quantity
            quantity = productService.checkAvailable(productId, itemEntity.getQuantity() + quantity);
            itemEntity.setQuantity(quantity);
            return cartDAO.updateCartItem(itemEntity, userId);
        }
    }

    public List<CartItemDTO> getItemFromCookies(Cookie[] cookies) {
        List<CartItemDTO> items = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("productId")) {
                int productId = Integer.parseInt(cookie.getName().substring("productId".length()));
                int quantity = Integer.parseInt(cookie.getValue());
                ProductDTO product = productService.getByIdWithAvatar(productId);
                items.add(CartItemDTO.builder().product(product).quantity(quantity).build());
            }
        }
        return items;
    }

    public Cookie addItemToCookie(Cookie[] cookies, CartItem cartItem) {
        int available;

        // Loop through cookies to handle products in the cart
        for (Cookie cookie : cookies) {
            // Find the corresponding product in the cookies
            if (cookie.getName().equals("productId" + cartItem.getProductId())) {
                // Increment quantity if found in the cookie
                int quantity = Integer.parseInt(cookie.getValue()) + cartItem.getQuantity();
                if (productService.checkProductValid(cartItem.getProductId(), quantity)) {
                    available = productService.checkAvailable(cartItem.getProductId(), quantity);
                    cookie.setValue(String.valueOf(available));
                    cookie.setMaxAge(5 * 24 * 60 * 60);
                    return cookie;
                } else
                    return null;
            }
        }

        // Add the product to cookies if not found in the existing cart
        if (productService.checkProductValid(cartItem.getProductId(), cartItem.getQuantity())) {
            available = productService.checkAvailable(cartItem.getProductId(), cartItem.getQuantity());
            Cookie cookie = new Cookie("productId" + cartItem.getProductId(), String.valueOf(available));
            cookie.setMaxAge(5 * 24 * 60 * 60);
            return cookie;
        } else
            return null;
    }

    public List<Cookie> updateItemInCookies(Cookie[] cookies, Map<String, String> map) {
        List<Cookie> cookieList = new ArrayList<>();
        // Loop through cookies to handle cart updates
        for (Cookie cookie : cookies) {
            Iterator<String> entries = map.keySet().iterator();
            if(!entries.hasNext())
                break;

            while (entries.hasNext()) {
                String key = entries.next();
                if (cookie.getName().equals("productId" + key)) {
                    // Extract productId from cookie name
                    int productId = Integer.parseInt(key);
                    int quantity = Integer.parseInt(map.get(key));

                    quantity = productService.checkAvailable(productId, quantity);
                    cookie.setValue(String.valueOf(quantity));
                    cookie.setMaxAge(5 * 24 * 60 * 60);
                    cookieList.add(cookie);

                    //to avoid processing it again in subsequent iterations
                    map.remove(key);
                    break;
                }
            }
        }
        return cookieList;
    }

    public List<Cookie> removeItemsInCookies(Cookie[] cookies, Integer productId) {
        List<Cookie> cookieList = Arrays.stream(cookies)
                .filter(cookie -> shouldRemoveCookie(cookie, productId))
                .peek(cookie -> cookie.setMaxAge(0))
                .collect(Collectors.toList());
        return cookieList;

    }

    private boolean shouldRemoveCookie(Cookie cookie, Integer productId) {
        //delete all product in cart
        if (productId == null || productId == -1) {
            return cookie.getName().startsWith("productId");
        } else {
            // delete a product
            return cookie.getName().equals("productId" + productId);
        }
    }

    public boolean deleteProduct(int productId, long userId) {
        return cartDAO.deleteProduct(productId, userId);
    }

    public boolean deleteAll(long userId) {
        return cartDAO.deleteAll(userId);
    }

    public boolean updateCart(Map<String, String> map, long userId) {
        try {
            boolean hasError = false;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                int productId = Integer.parseInt(entry.getKey());
                int quantity = Integer.parseInt(entry.getValue());
                if (!productService.checkProductValid(productId, quantity)) {
                    hasError = true;
                }
                quantity = productService.checkAvailable(productId, quantity);
                cartDAO.updateCartItem(CartItem.builder().productId(productId).quantity(quantity).build(), userId);

            }
            return !hasError;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
