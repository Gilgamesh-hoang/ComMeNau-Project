package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.service.InvoiceService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/invoiceDetail")
public class InvoiceDetailController extends HttpServlet {
    @Inject
    private InvoiceService invoiceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int invoiceId = Integer.parseInt(req.getParameter("id"));
        InvoiceDTO invoice = invoiceService.getInvoiceById(invoiceId);
        List<InvoiceItemDTO> listItems = invoiceService.getItemByInvoiceId(invoiceId);

        List<String> states = List.of(SystemConstant.INVOICE_PROCESSING, SystemConstant.INVOICE_TRANSPORTING,
                SystemConstant.INVOICE_DELIVERED, SystemConstant.INVOICE_CANCEL);

        req.setAttribute("listItems", listItems);
        req.setAttribute("invoice", invoice);
        req.setAttribute("invoiceActive", "");
        req.setAttribute("states", states);
        req.getRequestDispatcher("/admin/admin-order-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer invoiceId = null;
        String selectedStatus = req.getParameter("selectedStatus");
        boolean validateSelected = StringUtils.equalsAny(selectedStatus, SystemConstant.INVOICE_PROCESSING,
                SystemConstant.INVOICE_TRANSPORTING, SystemConstant.INVOICE_DELIVERED, SystemConstant.INVOICE_CANCEL);
        try {
            invoiceId = Integer.valueOf(req.getParameter("invoiceId"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (invoiceId != null && validateSelected) {
            boolean result = invoiceService.changeStatus(invoiceId, selectedStatus);
            resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
