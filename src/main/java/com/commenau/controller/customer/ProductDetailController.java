package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.ProductDTO;
import com.commenau.model.User;
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
    private ProductService productService;
    @Inject
    private WishlistService wishlistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] pathInfo = req.getPathInfo().split("/");
        int productId = 0;
        try {
            productId = Integer.valueOf(pathInfo[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (productId != 0) {
            if (req.getSession(false) != null && req.getSession(false).getAttribute("auth") != null) {
                long userId = ((User) req.getSession(false).getAttribute(SystemConstant.AUTH)).getId();
                req.setAttribute("userId", userId);
                if (wishlistService.existsItem(userId, productId)) {
                    req.setAttribute("inWishlists", true);
                }
            }

            ProductDTO productDTO = productService.getProductById(productId);

            req.setAttribute("product", productDTO);
            req.setAttribute("relativeProducts", productService.getRelativeProducts(productDTO.getId()));
            String currentPage = req.getRequestURL().toString();
            req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        }
        req.getRequestDispatcher("/customer/product-detail.jsp").forward(req, resp);

    }
}
