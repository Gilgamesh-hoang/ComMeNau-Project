package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.validate.Validator;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/change-password")
public class EditPasswordController extends HttpServlet {
    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/customer/dash-change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");
        boolean hasError = validate(newPassword, confirmPassword, currentPassword, user);
        if (!hasError) {
            user = userService.changePassword(newPassword, user);
            resp.setStatus(user != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean validate(String newPassword, String confirmPassword, String currentPassword, User user) {
        return Validator.isEmpty(newPassword) || Validator.isEmpty(currentPassword) ||
                Validator.isEmpty(confirmPassword) || !newPassword.equals(confirmPassword) ||
                !BCrypt.checkpw(currentPassword, user.getPassword());
    }
}

