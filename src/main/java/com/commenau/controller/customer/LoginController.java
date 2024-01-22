package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.UserDAO;
import com.commenau.model.User;
import com.commenau.service.RoleService;
import com.commenau.service.UserService;
import com.commenau.util.EncryptUtil;
import com.commenau.util.FormUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = "";
        String password = "";
        String rememberMe = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cUsername"))
                    username = cookie.getValue();
                else if (cookie.getName().equals("cPassword"))
                    password = cookie.getValue();
                else if (cookie.getName().equals("cRememberMe"))
                    rememberMe = cookie.getValue();
            }
        }
        request.setAttribute("username", username);
        request.setAttribute("password", EncryptUtil.decrypt(password));
        request.setAttribute("rememberMe", rememberMe);
        request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user data from the form
        User userForm = FormUtil.toModel(User.class, request);
        String password = userForm.getPassword();
        String rememberMe = request.getParameter("rememberMe");
        // Check for duplicate username and password and status

        User user = userService.signin(userForm);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute(SystemConstant.AUTH, user);
            // remember me
            Cookie cUsername = new Cookie("cUsername", user.getUsername());
            Cookie cPassword = new Cookie("cPassword", EncryptUtil.encrypt(password));
            Cookie cRememberMe = new Cookie("cRememberMe", rememberMe);
            cUsername.setMaxAge(5 * 24 * 60 * 60); //5 days

            if (rememberMe != null) {
                cPassword.setMaxAge(5 * 24 * 60 * 60); //5 days
                cRememberMe.setMaxAge(5 * 24 * 60 * 60); //5 days
            } else {
                cPassword.setMaxAge(0);
                cRememberMe.setMaxAge(0);
            }
            response.addCookie(cUsername);
            response.addCookie(cPassword);
            response.addCookie(cRememberMe);

            String previousPage = (String) request.getSession().getAttribute(SystemConstant.PRE_PAGE);
            if (previousPage != null && !previousPage.isEmpty()) {
                if (roleService.getById(user.getRoleId()).getName().equals(SystemConstant.ADMIN)) {
                    response.sendRedirect(request.getContextPath() + "/admin/home");
                    return;
                }
                response.sendRedirect(previousPage);
            } else {
                response.sendRedirect("login");
            }
        } else {
            request.setAttribute("signinError", "");
            request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
        }
    }

}
