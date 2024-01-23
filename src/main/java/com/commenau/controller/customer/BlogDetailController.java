package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.BlogReviewDTO;
import com.commenau.model.Blog;
import com.commenau.model.BlogReview;
import com.commenau.model.User;
import com.commenau.service.BlogReviewService;
import com.commenau.service.BlogService;
import com.commenau.util.FormUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blog-detail")
public class BlogDetailController extends HttpServlet {
    @Inject
    BlogService blogService;
    @Inject
    BlogReviewService blogReviewService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        try {
            int blogId = Integer.parseInt(idParam);
            Blog blog = blogService.getBlogById(blogId);
            if (blog != null) {
                List<BlogReviewDTO> reviews = blogReviewService.getReviewByBlogId(blogId);

                String currentPage = req.getRequestURL().toString();
                req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
                req.setAttribute("blog", blog);
                req.setAttribute("listBlogReview", reviews);
                req.setAttribute("numberReviews", blogReviewService.numberReviews(blogId));
            }
            req.getRequestDispatcher("/customer/blog-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BlogReview blogReview = FormUtil.toModel(BlogReview.class, request);
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        blogReview.setUserId(user.getId());
        blogReviewService.saveReview(blogReview);
        response.sendRedirect("blog-detail?id=" + blogReview.getBlogId());
    }
}
