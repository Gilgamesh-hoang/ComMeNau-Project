package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.model.User;
import com.commenau.service.InvoiceService;
import com.commenau.service.WishlistService;
import com.commenau.util.HttpUtil;

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

@WebServlet("/invoice-details")
public class InvoiceDetailController extends HttpServlet {
    @Inject
    private InvoiceService invoiceService;
    @Inject
    private WishlistService wishlistService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        int idParam = 0;
        try {
            idParam = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            e.getMessage();
            resp.getWriter().println("Not found Invoice !");
        }
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        List<InvoiceItemDTO> invoiceItemDTOs = invoiceService.getItemByInvoiceId(idParam);
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(idParam);

        //nav-left
        request.setAttribute("numWishlistItems", wishlistService.countWishlist(user.getId()));
        request.setAttribute("numInvoiceCanceled", invoiceService.countInvoiceByStatus(SystemConstant.INVOICE_CANCEL, user.getId()));
        request.setAttribute("totalItem", invoiceService.countAll(user.getId()));

        request.setAttribute("states", setInvoiceState(invoiceDTO));
        request.setAttribute("listInvoiceItemDTO", invoiceItemDTOs);
        request.setAttribute("invoiceDTO", invoiceDTO);
        request.getRequestDispatcher("/customer/dash-manage-order.jsp").forward(request, resp);

    }
    private Map<String, Boolean> setInvoiceState(InvoiceDTO invoiceDTO) {
        Map<String, Boolean> states = new LinkedHashMap<>();
        states.put(SystemConstant.INVOICE_PROCESSING, false);
        states.put(SystemConstant.INVOICE_TRANSPORTING, false);
        states.put(SystemConstant.INVOICE_DELIVERED, false);
        for (String key : states.keySet()) {
            if (invoiceDTO.getStatus().equals(SystemConstant.INVOICE_CANCEL)) break;
            if (invoiceDTO.getStatus().equals(key)) {
                states.put(key, true);
                break;
            }
            states.put(key, true);
        }
        return states;
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int invoiceId = HttpUtil.of(req.getReader()).toModel(Integer.class);
        resp.setStatus(invoiceService.changeStatus(invoiceId, SystemConstant.INVOICE_CANCEL)
                ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }


}
