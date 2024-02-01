package com.commenau.service;

import com.commenau.dao.*;
import com.commenau.dto.ProductDTO;
import com.commenau.mapper.ProductMapper;
import com.commenau.model.Product;
import com.commenau.pagination.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductReviewDAO reviewDAO;
    @Inject
    private InvoiceItemDAO itemDAO;
    @Inject
    private WishListDAO wishListDAO;
    @Inject
    private CartDAO cartDAO;
    @Inject
    private CategoryDAO categoryDao;
    @Inject
    private ProductImageDAO productImageDAO;
    @Inject
    private CancelProductDAO cancelDAO;
    @Inject
    private ProductMapper productMapper;

    public ProductDTO getByIdWithAvatar(int id) {
        Product product = productDAO.findOneById(id);
        return getProductDTOFromProduct(product, false);
    }

    public int countProductsInCategory(int categoryId) {
        return productDAO.countProductsInCategory(categoryId);
    }

    public ProductDTO getProductById(int id) {
        Product product = productDAO.findOneById(id);
        return getProductDTOFromProduct(product, true);
    }


    public List<ProductDTO> getNewestProducts(int limit) {
        List<Product> products = productDAO.findNewestProducts(limit);
        return products.stream().map(product -> getProductDTOFromProduct(product, false))
                .collect(Collectors.toList());

    }

    public List<ProductDTO> getRelativeProducts(int productId) {
        List<Product> products = productDAO.findRelativeProductsById(productId);
        return products.stream().map(product -> getProductDTOFromProduct(product, false))
                .collect(Collectors.toList());

    }

    public List<ProductDTO> getProductsByCategoryId(int categoryId, PageRequest pageRequest) {
        List<Product> products = productDAO.findByCategoryId(categoryId, pageRequest);
        return products.stream().map(product -> getProductDTOFromProduct(product, false))
                .collect(Collectors.toList());
    }


    public List<ProductDTO> getByKeyWord(PageRequest pageRequest, String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return getAll(pageRequest);
        else {
            List<Product> products = productDAO.findByKeyWord(pageRequest, keyWord.trim());
            return products.stream().map(product -> getProductDTOFromProduct(product, false))
                    .collect(Collectors.toList());
        }
    }


    public int countAll() {
        return productDAO.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return countAll();
        return productDAO.countByKeyWord(keyWord);
    }

    public List<ProductDTO> getAll(PageRequest pageRequest) {
        List<Product> products = productDAO.findAll(pageRequest);
        return products.stream().map(product -> getProductDTOFromProduct(product, false))
                .collect(Collectors.toList());
    }

    public int save(Product product) {
        product.setDiscount(product.getDiscount() / 100);
        if (product.getId() == 0)
            return productDAO.save(product);
        else if (productDAO.update(product) > 0)
            return product.getId();
        return -1;
    }

    public boolean deleteByIds(Integer[] ids, String realPath) {
        try {
            for (Integer id : ids) {
                List<String> allImageByProductId = productImageDAO.getAllImageByProductId(id);

                cancelDAO.deleteByProductId(id);//delete products in cancel_products
                cartDAO.deleteByProductId(id);//delete product in product_images
                productImageDAO.deleteByProductId(id);//delete product in product_reviews
                reviewDAO.deleteByProductId(id);//delete product in wishlists
                wishListDAO.deleteByProductId(id);//delete product in invoice_items
                itemDAO.updateProductId(id);//delete product in products

                //delete product's images
                allImageByProductId.forEach(img -> {
                    File file = new File(realPath + File.separator + img);
                    boolean delete = file.delete();
                });

                productDAO.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Return false immediately if any product deletion fails
            return false;
        }
    }


    public int checkAvailable(int productId, int quantity) {
        Product product = productDAO.findOneById(productId);
        return (product.getAvailable() - quantity < 0) ? product.getAvailable() : quantity;
    }

    public boolean checkProductValid(int productId, int quantity) {
        Product product = productDAO.findOneById(productId);
        return product.getAvailable() > 0 && quantity >= 1 && product.isStatus() && product.getAvailable() >= quantity;
    }

    public boolean setDefaultAvailable(Integer[] ids) {
        if (ids.length == 0)
            return productDAO.setDefaultAvailable();
        else return productDAO.setDefaultAvailable(ids);
    }

    private ProductDTO getProductDTOFromProduct(Product product, boolean isGetImages) {
        ProductDTO dto = productMapper.toDTO(product, ProductDTO.class);
        dto.setCategoryName(categoryDao.getNameById(product.getCategoryId()));
        dto.setAvatar(productImageDAO.findAvatarByProductId(dto.getId()));
        dto.setRate(productDAO.averageRating(product.getId()));
        dto.setAmountOfReview(productDAO.countProductReviewsById(product.getId()));
        if (isGetImages)
            dto.setImages(productImageDAO.getAllImageByProductId(product.getId()));
        return dto;
    }

}
