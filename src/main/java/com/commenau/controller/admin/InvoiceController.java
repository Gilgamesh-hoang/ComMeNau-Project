package com.commenau.controller.admin;

import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.InvoiceService;
import com.commenau.util.PagingUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/invoices")
public class InvoiceController extends HttpServlet {
    @Inject
    private InvoiceService invoiceService;
    private static final int maxPageItem = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String pageStr = request.getParameter("page");

        //paging
        int totalItem = invoiceService.countAll();
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        List<Sorter> sorters = List.of(new Sorter("createdAt", "DESC"));
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("invoiceActive","");
        request.setAttribute("invoices", invoiceService.getAllInvoice(pageRequest));
        request.getRequestDispatcher("/admin/admin-order.jsp").forward(request, resp);

    }
}
