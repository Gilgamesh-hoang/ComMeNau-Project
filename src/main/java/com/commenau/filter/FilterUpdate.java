package com.commenau.filter;

import com.commenau.service.SearchService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@WebFilter("/*")
public class FilterUpdate implements Filter {
    SearchService searchService;
    private static String paths[] = {};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        for(var x : paths){

            if(Pattern.matches(x,((HttpServletRequest) request).getServletPath())){
                searchService.updateInformationSearching();
            }
        }

        chain.doFilter(request,response);
    }
}
