package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/updateUser")
public class UpdateUserController extends HttpServlet {
    @Inject
    UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        Long userId = HttpUtil.of(req.getReader()).toModel(Long.class); // geted userId when click button
        int numCurrentPage = req.getParameter("nextPage") == null ? 1 : Integer.parseInt(req.getParameter("nextPage"));
        if (userId != null && userId != 0) {
            boolean result = userService.lockOrUnlock(userId); //
            String currentPage = req.getRequestURL().toString();
            resp.getWriter().write(gson.toJson(""));
        }
    }
}
