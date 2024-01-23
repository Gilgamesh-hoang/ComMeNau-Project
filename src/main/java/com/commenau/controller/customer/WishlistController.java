package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.WishlistItemDTO;
import com.commenau.model.User;
import com.commenau.service.WishlistService;
import com.commenau.util.HttpUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/wishlist")
public class WishlistController extends HttpServlet {
    @Inject
    private WishlistService wishlistService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);

        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        List<WishlistItemDTO> wishlist = wishlistService.getWishlist(user.getId());

        if (wishlist.isEmpty()) {
            req.getRequestDispatcher("/customer/empty-wishlist.jsp").forward(req, resp);
        } else {
            req.setAttribute("wishlist", wishlist);
            req.getRequestDispatcher("/customer/wishlist.jsp").forward(req, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            int productId = Integer.parseInt(req.getParameter("productId"));
            wishlistService.addItem(userId, productId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = ((User) req.getSession().getAttribute(SystemConstant.AUTH));
        Integer productId = HttpUtil.of(req.getReader()).toModel(Integer.class);
        boolean result;
        if (productId != null && productId > 0) {
            result = wishlistService.deleteItem(user.getId(), productId);
        } else {
            result = wishlistService.resetAll(user.getId());
        }
        resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
