package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.service.InvoiceService;
import com.commenau.util.HttpUtil;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@WebServlet("/admin/invoiceDetail")
public class InvoiceDetailController extends HttpServlet {
    @Inject
    InvoiceService invoiceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int invoiceId = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("invoiceActive", "");
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(invoiceId);
        req.setAttribute("invoiceOf", invoiceDTO);
        List<InvoiceItemDTO> list = invoiceService.getAllInvoiceItemDTOByInvoiceId(invoiceId);
        req.setAttribute("listInvoiceDetail", list);
        List<String> states = new ArrayList<>();
        states.add(SystemConstant.INVOICE_PROCESSING);
        states.add(SystemConstant.INVOICE_SHIPPING);
        states.add(SystemConstant.INVOICE_SHIPPED);
        states.add(SystemConstant.INVOICE_CANCEL);
        req.setAttribute("states", states);
        req.getRequestDispatcher("/admin/admin-order-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String str = HttpUtil.of(req.getReader()).toModel(String.class);
        StringTokenizer reader = new StringTokenizer(str, "-");
        Integer invoiceId = Integer.valueOf(reader.nextToken()); // geted invoiceId when click button_save
        String selectedStatus = reader.nextToken(); // geted selectedStatus when click button_save
        if (invoiceId != null) {
            boolean result = invoiceService.changeInvoiceStatus(invoiceId, selectedStatus);
            resp.getWriter().write(gson.toJson(""));
        }
    }
}
