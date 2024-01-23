package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.Blog;
import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;
import com.commenau.service.BlogService;
import com.commenau.util.PagingUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blogs")
public class BlogController extends HttpServlet {
    @Inject
    BlogService blogService;
    private static final int maxPageItem = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");

        //newest
        List<Blog> newestBlogs = blogService.getBlogNewest(3);
        request.setAttribute("newestBlogs", newestBlogs);

        //paging
        int totalItem = blogService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        List<Sorter> sorters = List.of(new Sorter("createdAt", "DESC"));
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("blogs", blogService.getByKeyWord(pageRequest, keyWord));
        String currentPage = request.getRequestURL().toString();
        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.getRequestDispatcher("/customer/blog.jsp").forward(request, response);

    }

}
