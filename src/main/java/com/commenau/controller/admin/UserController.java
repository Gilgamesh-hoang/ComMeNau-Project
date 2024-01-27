package com.commenau.controller.admin;

import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.UserService;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/user")
public class UserController extends HttpServlet {
    @Inject
    private UserService userService;
    private static final int maxPageItem = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");

        //paging
        int totalItem = userService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        List<Sorter> sorters = List.of(new Sorter("createdAt", "DESC"));
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("userActive", "");
        request.setAttribute("listCustomer", userService.getByKeyWord(pageRequest, keyWord));
        request.getRequestDispatcher("/admin/admin-user.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = HttpUtil.of(req.getReader()).toModel(Long.class);
        if (userId != null && userId > 0) {
            boolean result = userService.lockOrUnlock(userId);
            resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
