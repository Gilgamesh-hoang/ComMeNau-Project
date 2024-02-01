package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.ProductDTO;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.ProductService;
import com.commenau.service.WishlistService;
import com.commenau.util.PagingUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/menu/filter")
public class FilterProductController extends HttpServlet {
    @Inject
    private ProductService productService;
    @Inject
    private WishlistService wishlistService;
    private static final int maxPageItem = 9;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        String pageStr = request.getParameter("page");
        String sortName = request.getParameter("sortName");
        String sortBy = request.getParameter("sortBy");

        int categoryId = 1;
        try {
            categoryId = Integer.parseInt(request.getParameter("id"));
            categoryId = (categoryId <= 0) ? 1 : categoryId;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //paging
        int totalItem = productService.countProductsInCategory(categoryId);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        Sorter sorter = StringUtils.isBlank(sortName) ? new Sorter("createdAt", "DESC") :
                new Sorter(sortName, sortBy);
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(List.of(sorter)).build();

        List<ProductDTO> products = productService.getProductsByCategoryId(categoryId, pageRequest);
        if (user != null) {
            for (ProductDTO product : products) {
                product.setWishlist(wishlistService.existsItem(user.getId(), product.getId()));
            }
        }
        response.getWriter().write(new Gson().toJson(products));
    }
}
