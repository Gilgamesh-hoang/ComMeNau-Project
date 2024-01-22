package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.Invoice;
import com.commenau.model.User;
import com.commenau.service.CartService;
import com.commenau.service.InvoiceService;
import com.commenau.service.ProductService;
import com.commenau.util.FormUtil;
import com.commenau.validate.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/payments")
public class CheckoutController extends HttpServlet {
    @Inject
    private CartService cartService;
    @Inject
    private InvoiceService invoiceService;
    @Inject
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        String paymentMethod = request.getParameter("paymentMethod");

        if (!StringUtils.isBlank(paymentMethod) && paymentMethod.equals("VNPAY")) {
            doPost(request, response);
            return;
        }

        if (user != null) {
            //using db
            List<CartItemDTO> items = cartService.getCartByUserId(user.getId());
            if (items.isEmpty()) {
                response.sendRedirect("carts");
                return;
            } else {
                long totalPrice = cartService.totalPrice(items);
                request.setAttribute("cart", items);
                request.setAttribute("totalPrice", totalPrice);
            }
            request.setAttribute("fullName", user.getLastName() + " " + user.getFirstName());
            request.setAttribute("phoneNumber", user.getPhoneNumber());
            request.setAttribute("address", user.getAddress());
            request.setAttribute("email", user.getEmail());
        } else {
            //get products from cookie
            List<CartItemDTO> items = new ArrayList<>();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("productId")) {
                    int productId = Integer.parseInt(cookie.getName().substring("productId".length()));
                    int quantity = Integer.parseInt(cookie.getValue());
                    ProductViewDTO product = productService.getByIdWithAvatar(productId);
                    items.add(CartItemDTO.builder().product(product).quantity(quantity).build());
                }
            }
            if (items.isEmpty()) {
                response.sendRedirect("carts");
                return;
            } else {
                long totalPrice = cartService.totalPrice(items);
                request.setAttribute("cart", items);
                request.setAttribute("totalPrice", totalPrice);
            }
        }
        request.getRequestDispatcher("/customer/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        Invoice invoice = FormUtil.toModel(Invoice.class, request);
        boolean hasError = validate(invoice);


        boolean isVnPayAction = !StringUtils.isBlank(invoice.getPaymentMethod()) && invoice.getPaymentMethod().equals("VNPAY");

        boolean isSuccess = false;

        if (!hasError) {
            if (user != null) {
                invoice.setUserId(user.getId());
                isSuccess = invoiceService.saveInvoice(invoice);
            } else {
                // Using cookie
                isSuccess = invoiceService.saveInvoiceWithCookie(invoice, request.getCookies());
                if (isSuccess) {
                    // Delete all cart items in cookie
                    Cookie[] cookies = request.getCookies();
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().startsWith("productId")) {
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }

        if (isVnPayAction) {
            response.sendRedirect("carts");
            return;
        }

        if (!hasError && isSuccess) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (!hasError && !isSuccess) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    private boolean validate(Invoice invoice) {
        if (Validator.isEmpty(invoice.getFullName()) || Validator.isEmpty(invoice.getAddress()) ||
                !Validator.isValidEmail(invoice.getEmail()) || !Validator.isValidPhoneNumber(invoice.getPhoneNumber()))
            return true;
        return false;
    }
}
