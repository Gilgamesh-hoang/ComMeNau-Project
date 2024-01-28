package com.commenau.controller.customer;

import com.commenau.service.SearchService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/search")
public class SearchController extends HttpServlet {
    @Inject
   private SearchService searchService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String query = req.getParameter("query");
        if(StringUtils.isBlank(query)) return;
        resp.getWriter().write(gson.toJson(searchService.search(query)));
    }
}
