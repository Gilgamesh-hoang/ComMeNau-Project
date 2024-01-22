package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.FormUtil;
import com.commenau.util.HttpUtil;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/change-profile")
public class EditProfileController extends HttpServlet {
    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/customer/dash-edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = HttpUtil.of(req.getReader()).toModel(User.class);
        boolean hasError = validate(user);
        if (!hasError) {
            boolean result = userService.updateProfile(user);
            req.getSession().setAttribute(SystemConstant.AUTH, user);
            resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean validate(User user) {
        return Validator.isEmpty(user.getFirstName()) || Validator.isEmpty(user.getLastName()) ||
                Validator.isEmpty(user.getAddress()) || !Validator.isValidPhoneNumber(user.getPhoneNumber());

    }
}
