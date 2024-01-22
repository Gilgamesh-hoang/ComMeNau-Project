package com.commenau.controller.customer;


import com.commenau.constant.SystemConstant;
import com.commenau.dto.ProductDTO;
import com.commenau.model.User;
import com.commenau.service.ProductReviewService;
import com.commenau.service.ProductService;
import com.commenau.service.WishlistService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/product/*")
public class ProductDetailController extends HttpServlet {
    @Inject
    ProductService productService;
    @Inject
    WishlistService wishlistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] pathInfo = req.getPathInfo().split("/");
        int productId = Integer.valueOf(pathInfo[1]);

        if (req.getSession(false) != null && req.getSession(false).getAttribute("auth") != null) {
            int userId = Math.toIntExact(((User) req.getSession(false).getAttribute("auth")).getId());
            req.setAttribute("userId", userId);
            if (wishlistService.existsItem(userId, Integer.valueOf(productId))) {
                req.setAttribute("inWishlists", true);
            }
        }

        ProductDTO productDTO = productService.getProductById(productId);

        req.setAttribute("product", productDTO);
        req.setAttribute("relativeProducts", productService.getRelativeProductViewByID(productDTO.getId()));
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        req.getRequestDispatcher("/customer/product-detail.jsp").forward(req, resp);

    }
}
