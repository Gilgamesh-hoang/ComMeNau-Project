package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.ProductReview;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.ProductReviewService;
import com.commenau.util.FormUtil;
import com.commenau.validate.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/review")
public class ProductReviewController extends HttpServlet {
    @Inject
    private ProductReviewService productReviewService;
    private static final int maxPageItem = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper json = new ObjectMapper();
        int productId = Integer.parseInt(req.getParameter("id"));
        int page = Integer.parseInt(req.getParameter("page"));
        String sortBy = req.getParameter("sortBy");
        String sort = req.getParameter("sort");

        //paging
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(List.of(new Sorter(sortBy, sort))).build();
        resp.setContentType("application/json");
        resp.getWriter().write(json.writeValueAsString(productReviewService.getReviewByProductId(productId, pageRequest)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession(false).getAttribute(SystemConstant.AUTH);
        String ratingStr = req.getParameter("rating");
        String productIdStr = req.getParameter("productId");
        String content = req.getParameter("content");
        if (user != null && validate(content, ratingStr, productIdStr)) {
            ProductReview review = FormUtil.toModel(ProductReview.class, req);
            review.setUserId(user.getId());
            boolean result = productReviewService.save(review);
            resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean validate(String content, String ratingStr, String productIdStr) {
        try {
            int rating = Integer.parseInt(ratingStr);
            int productId = Integer.parseInt(productIdStr);

            if (!Validator.between(rating, 1, 5))
                return false;
            if (!Validator.between(productId, 1, Integer.MAX_VALUE))
                return false;
            if (Validator.isEmpty(content))
                return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

}
