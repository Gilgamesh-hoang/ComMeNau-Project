package com.commenau.service;

import com.commenau.dao.CategoryDAO;
import com.commenau.dao.WishListDAO;
import com.commenau.dto.ProductDTO;
import com.commenau.dto.WishlistItemDTO;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class WishlistService {
    @Inject
    private WishListDAO wishlistDAO;
    @Inject
    private ProductService productService;
    @Inject
    private CategoryDAO categoryDAO;

    public List<WishlistItemDTO> getWishlist(Long userId) {
        return wishlistDAO.findWishListByUser(userId).stream().map(item -> {
            ProductDTO product = productService.getByIdWithAvatar(item.getProductId());
            product.setCategoryName(categoryDAO.getNameById(product.getCategoryId()));
            return WishlistItemDTO.builder().product(product).build();
        }).collect(Collectors.toList());
    }
    public int countWishlist(Long userId) {
        return wishlistDAO.countWishlist(userId);
    }
    public boolean existsItem(long userId, int productId) {
        return wishlistDAO.existsItem(productId, userId);
    }

    public void addItem(int userID, int productId) {
        wishlistDAO.saveItem(userID, productId);
    }

    public boolean deleteItem(long userID, int productId) {
        return wishlistDAO.deleteItem(userID, productId);
    }

    public boolean resetAll(Long userId) {
        return wishlistDAO.resetAll(userId);
    }
}
