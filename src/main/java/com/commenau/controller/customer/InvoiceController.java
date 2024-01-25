package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.InvoiceService;
import com.commenau.service.WishlistService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/invoices")
public class InvoiceController extends HttpServlet {
    @Inject
    private InvoiceService invoiceService;
    @Inject
    private  WishlistService wishlistService;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        String sortBy = request.getParameter("sortBy");
        int totalItem = invoiceService.countAll(user.getId());

        //sort
        Sorter sorter = StringUtils.isBlank(sortBy) ? new Sorter("createdAt", "DESC") :
                new Sorter(sortBy.split("_")[0], sortBy.split("_")[1]);
        List<Sorter> sorters = List.of(sorter);
        PageRequest pageRequest = PageRequest.builder().sorters(sorters).build();
        List<InvoiceDTO> invoices = invoiceService.getAllInvoice(pageRequest, user.getId());

        //nav-left
        request.setAttribute("numWishlistItems", wishlistService.countWishlist(user.getId()));
        request.setAttribute("numInvoiceCanceled", invoiceService.countInvoiceByStatus(SystemConstant.INVOICE_CANCEL, user.getId()));
        request.setAttribute("totalItem", totalItem);
        //main
        request.setAttribute("sort", initSortBy());
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("invoices", invoices);
        request.getRequestDispatcher("/customer/dash-my-order.jsp").forward(request, resp);
    }

    private Map<String, String> initSortBy() {
        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("createdAt_DESC", "Đơn mới nhất");
        sort.put("createdAt_ASC", "Đơn cũ nhất");
        return sort;
    }
}
