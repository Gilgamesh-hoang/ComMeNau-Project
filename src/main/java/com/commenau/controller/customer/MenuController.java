package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.service.CategoryService;
import com.commenau.service.ProductService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/menu")
public class MenuController extends HttpServlet {
    @Inject
    private ProductService productService;
    @Inject
    private CategoryService categoryService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryId ;
        if(req.getParameter("categoryId") != null){
            categoryId = Integer.parseInt(req.getParameter("categoryId"));
        }
        else{
            categoryId = 1;
        }
        req.setAttribute("total" , productService.countProductsInCategory(categoryId));
        req.setAttribute("categories" , categoryService.getAllCategories());
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        req.getRequestDispatcher("/customer/filter.jsp").forward(req,resp);
    }
}
