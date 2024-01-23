package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.model.User;
import com.commenau.service.InvoiceService;
import com.commenau.service.WishlistService;
import com.commenau.util.HttpUtil;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/invoice-details")
public class InvoiceDetailController extends HttpServlet {
    @Inject
    InvoiceService invoiceService;
    @Inject
    WishlistService wishlistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int idParam = Integer.parseInt(req.getParameter("id"));
            User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
            List<InvoiceDTO> invoicedtos = invoiceService.getAllInvoiceDTOById(user.getId());
            List<InvoiceItemDTO> invoiceItemDTOs = invoiceService.getAllInvoiceItemDTOByInvoiceId(idParam);
            InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(idParam);
            //states
            Map<String, Boolean> states = new LinkedHashMap<>();
            states.put(SystemConstant.INVOICE_PROCESSING, false);
            states.put(SystemConstant.INVOICE_SHIPPING, false);
            states.put(SystemConstant.INVOICE_SHIPPED, false);
            for (String key : states.keySet()) {
                if (invoiceDTO.getStatus().equals(SystemConstant.INVOICE_CANCEL)) break;
                if (invoiceDTO.getStatus().equals(key)) {
                    states.put(key, true);
                    break;
                }
                states.put(key, true);
            }
            int count = 0;
            for (InvoiceDTO i : invoicedtos) {
                if (i.getStatus().equals(SystemConstant.INVOICE_CANCEL)) count++;
            }
            req.setAttribute("numWishlistItems", wishlistService.getWishlist(user.getId()).size());
            req.setAttribute("numInvoiceCanceled", count);
            req.setAttribute("states", states);
            req.setAttribute("sizeListInvoiceDTO", invoicedtos.size());
            req.setAttribute("fullNameOfUser", user.fullName());
            req.setAttribute("listInvoiceItemDTO", invoiceItemDTOs);
            req.setAttribute("invoiceDTO", invoiceDTO);
            req.getRequestDispatcher("/customer/dash-manage-order.jsp").forward(req, resp);
        } catch (Exception e) {
            e.getMessage();
            resp.getWriter().println("Not found Invoice !");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int invoiceId = HttpUtil.of(req.getReader()).toModel(Integer.class);
        Gson gson = new Gson();
        boolean result = invoiceService.changeInvoiceStatus(invoiceId, SystemConstant.INVOICE_CANCEL);
        resp.getWriter().write(gson.toJson(""));
    }
}
