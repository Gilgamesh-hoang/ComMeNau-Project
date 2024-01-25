package com.commenau.controller.admin;

import com.commenau.model.Category;
import com.commenau.pagination.PageRequest;
import com.commenau.service.CategoryService;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/categories")
public class CategoryController extends HttpServlet {
    @Inject
    private CategoryService categoryService;
    private static final int maxPageItem = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");

        //paging
        int totalItem = categoryService.getTotalItem();
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("categoryActive", "");
        request.setAttribute("categories", categoryService.getAll(pageRequest));
        request.getRequestDispatcher("/admin/admin-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category category = HttpUtil.of(request.getReader()).toModel(Category.class);
        boolean hasError = validate(category);
        if (!hasError) {
            if (category.getId() != 0) {
                // update category
                boolean result = categoryService.update(category);
                response.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                //add new category
                boolean result = categoryService.save(category);
                response.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        boolean result = categoryService.detele(ids);
        response.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private boolean validate(Category category) {
        return Validator.isEmpty(category.getName()) || Validator.isEmpty(category.getCode());
    }
}
