package com.commenau.service;

import com.commenau.dao.*;
import com.commenau.dto.ProductDTO;
import com.commenau.dto.ProductRelativeViewDTO;
import com.commenau.mapper.ProductMapper;
import com.commenau.model.Product;
import com.commenau.paging.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements Pageable<ProductDTO> {

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

    public ProductDTO getByIdWithAvatar(int id) {
        Product product = productDAO.findOneById(id);
        ProductDTO productDTO = ProductMapper.INSTANCE.toDTO(product);
        productDTO.setImages(List.of(productImageDAO.findAvatarByProductId(product.getId())));
        return productDTO;

    }

    public int countProductsInCategory(int categoryId) {
        return productDAO.countProductsInCategory(categoryId);
    }

    public ProductDTO getProductById(int id) {
        Product product = productDAO.findOneById(id);
        ProductDTO productDTO = ProductMapper.INSTANCE.toDTO(product);

        productDTO.setImages(productImageDAO.getAllImageByProductId(id));
        productDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(product.getId())));
        productDTO.setRate(averageRating(id));

        return productDTO;

    }


    public int averageRating(int id) {
        return productDAO.averageRating(id);
    }

    public int countProductReviewsById(int id) {
        return productDAO.countProductReviewsById(id);
    }

    public List<ProductRelativeViewDTO> getNewRelativeProductView() {
        List<Product> products = productDAO.getNewRelativeProductView();
        List<ProductRelativeViewDTO> views = new ArrayList<>();
        for (var x : products) {
            ProductRelativeViewDTO productViewDTO =
                    ProductRelativeViewDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .productName(x.getName())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productViewDTO.setCategoryName(categoryName);
            productViewDTO.setRating(averageRating(Math.toIntExact(x.getId())));
            productViewDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(x.getId())));
            productViewDTO.setImage(productImageDAO.findAvatarByProductId(x.getId()));
            views.add(productViewDTO);
        }
        return views;
    }

    public List<ProductRelativeViewDTO> getRelativeProductViewByID(int productId) {
        List<Product> products = productDAO.getRelativeProductViewByID(productId);
        if (products == null) return null;
        List<ProductRelativeViewDTO> views = new ArrayList<>();
        for (var x : products) {
            ProductRelativeViewDTO productViewDTO =
                    ProductRelativeViewDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .productName(x.getName())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productViewDTO.setCategoryName(categoryName);
            productViewDTO.setRating(averageRating(Math.toIntExact(x.getId())));
            productViewDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(x.getId())));
            productViewDTO.setImage(productImageDAO.findAvatarByProductId(x.getId()));
            views.add(productViewDTO);
        }
        return views;
    }

    @Override
    public List<ProductDTO> getPage(int categoryId, int size, int page, String sortBy, String sort) {
        List<Product> products;
        if (sortBy == null) {
            products = productDAO.getProductViewPage(categoryId, size, page);
        } else if (!sortBy.equals("rate")) {
            products = productDAO.getProductViewPage(categoryId, size, page, sortBy, sort);
        } else {
            products = productDAO.getProductViewPageSortByRating(categoryId, size, page, sort);

        }
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (var x : products) {

            ProductDTO productDTO =
                    ProductDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .name(x.getName())
                            .description(x.getDescription())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .rate((int) x.getRate())
                            .available(x.getAvailable())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productDTO.setCategoryName(categoryName);
            productDTO.setAmountOfReview(this.countProductReviewsById(x.getId()));
            productDTO.setAvatar(productImageDAO.findAvatarByProductId(x.getId()));
            productDTOS.add(productDTO);
        }


        return productDTOS;

    }

    public List<ProductDTO> getByKeyWord(PageRequest pageRequest, String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return getAll(pageRequest);
        else {
            List<Product> products = productDAO.findByKeyWord(pageRequest, keyWord.trim());
            return products.stream().map(product -> {
                        ProductDTO dto = ProductMapper.INSTANCE.toDTO(product);
                        dto.setCategoryName(categoryDao.getNameById(product.getCategoryId()));
                        dto.setAvatar(productImageDAO.findAvatarByProductId(dto.getId()));
                        return dto;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public List<ProductDTO> getPage(int id, int size, int page) {
        return this.getPage(id, size, page, null, null);
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
        return productDAO.findAll(pageRequest);
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
}
