package com.commenau.service;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.*;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.mail.MailService;
import com.commenau.model.Invoice;
import com.commenau.model.InvoiceItem;
import com.commenau.model.Product;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.*;

public class InvoiceService {
    @Inject
    private InvoiceDAO invoiceDAO;
    @Inject
    private InvoiceItemDAO invoiceItemDAO;
    @Inject
    private InvoiceStatusDAO invoiceStatusDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductImageDAO productImageDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private CartDAO cartDAO;
    @Inject
    private MailService mail;

    public List<InvoiceDTO> getAllInvoiceDTOById(Long userId) {
        List<InvoiceDTO> re = new ArrayList<>();
        for (Invoice in : invoiceDAO.getAllInvoiceById(userId)) {
            double total = 0;
            double shippingFee = (invoiceDAO.getInvoiceById(in.getId()).getShippingFee() == null) ? 0 : invoiceDAO.getInvoiceById(in.getId()).getShippingFee();
            for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(in.getId())) {
                total += invoiceItem.getPrice() * invoiceItem.getQuantity();
            }
            InvoiceDTO invoicedto = InvoiceDTO.builder()
                    .id(in.getId())
                    .updatedAt(invoiceStatusDAO.getStatusByInvoice(in.getId()).getCreatedAt())
                    .status(invoiceStatusDAO.getStatusByInvoice(in.getId()).getStatus())
                    .total(total + shippingFee)
                    .build();
            re.add(invoicedto);
        }
        return re;
    }

    public List<InvoiceItemDTO> getAllInvoiceItemDTOByInvoiceId(int invoiceId) {
        List<InvoiceItemDTO> re = new ArrayList<>();
        for (InvoiceItem in : invoiceItemDAO.getAllInvoiceItemById(invoiceId)) {
            InvoiceItemDTO invoiceItemDTO = InvoiceItemDTO.builder()
                    .image(productImageDAO.findAvatarByProductId(in.getProductId()))
                    .name(productDAO.findOneById(in.getProductId()).getName())
                    .quantity(in.getQuantity())
                    .price(in.getPrice())
                    .build();
            re.add(invoiceItemDTO);
        }
        return re;
    }

    public InvoiceDTO getInvoiceDTOById(int invoiceId) {
        double total = 0;
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        double shippingFee = invoice.getShippingFee() == null ? 0 : invoice.getShippingFee();
        for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(invoiceId)) {
            total += invoiceItem.getPrice() * invoiceItem.getQuantity();
        }
        return InvoiceDTO.builder().id(invoiceId)
                .userFullName(invoice.getFullName())
                .userEmail(invoice.getEmail())
                .status(invoiceStatusDAO.getStatusByInvoice(invoiceId).getStatus())
                .updatedAt(invoiceStatusDAO.getStatusByInvoice(invoiceId).getCreatedAt())
                .createdAt(invoice.getCreatedAt())
                .shippingFee(shippingFee)
                .total(total)
                .address(invoice.getAddress())
                .phoneNumber(invoice.getPhoneNumber())
                .paymentMethod(invoice.getPaymentMethod())
                .build();
    }

    public List<InvoiceDTO> getAllInvoicePaged(int nextPage, int pageSize) {
        List<InvoiceDTO> re = new ArrayList<>();
        for (Invoice i : invoiceDAO.getAllInvoicePaged(nextPage, pageSize)) {
            double total = 0;
            for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(i.getId())) {
                total += invoiceItem.getPrice() * invoiceItem.getQuantity();
            }
            double shippingFee = i.getShippingFee() == null ? 0 : i.getShippingFee();
            re.add(new InvoiceDTO().builder().id(i.getId())
                    .userFullName(i.getFullName())
                    .total(total)
                    .shippingFee(shippingFee)
                    .phoneNumber(i.getPhoneNumber())
                    .status(invoiceStatusDAO.getStatusByInvoice(i.getId()).getStatus())
                    .build());
        }
        return re;
    }

    public List<Invoice> getAllInvoice() {
        return invoiceDAO.getAllInvoice();
    }

    public List<InvoiceDTO> get10InvoiceDTOById(Long userId) {
        List<InvoiceDTO> re = new ArrayList<>();
        for (Invoice in : invoiceDAO.get10InvoiceById(userId)) {
            double total = 0;
            double shippingFee = (invoiceDAO.getInvoiceById(in.getId()).getShippingFee() == null) ? 0 : invoiceDAO.getInvoiceById(in.getId()).getShippingFee();
            for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(in.getId())) {
                total += invoiceItem.getPrice() * invoiceItem.getQuantity();
            }
            InvoiceDTO invoicedto = InvoiceDTO.builder()
                    .id(in.getId())
                    .updatedAt(invoiceStatusDAO.getStatusByInvoice(in.getId()).getCreatedAt())
                    .status(invoiceStatusDAO.getStatusByInvoice(in.getId()).getStatus())
                    .total(total + shippingFee)
                    .build();
            re.add(invoicedto);
        }
        return re;
    }

    public Map<String, Integer> bestSaleProduct() {
        Map<String, Integer> map = new HashMap<>();
        InvoiceItem item = invoiceItemDAO.getBestSellingProduct();
        Product product = productDAO.findOneById(item.getProductId());
        map.put(product.getName(), item.getQuantity());
        return map;
    }

    public int sellingOfDay() {
        return Optional.ofNullable(invoiceDAO.sellingOfDay()).orElse(0);
    }

    public int sellingOfMonth() {
        return Optional.ofNullable(invoiceDAO.sellingOfMonth()).orElse(0);
    }

    public double revenueOfDay() {
        return Optional.ofNullable(invoiceDAO.revenueOfDay()).orElse(0.0);
    }

    public double revenueOfMonth() {
        return Optional.ofNullable(invoiceDAO.revenueOfMonth()).orElse(0.0);
    }

    public boolean saveInvoice(Invoice invoice) {
        int invoiceId = invoiceDAO.save(invoice);
        if (invoiceId == 0)
            return false;
        //Transfer products from shopping cart to invoice
        boolean isTranferInvoiceItem = invoiceItemDAO.transferProductsFromCartToInvoice(invoice.getUserId(), invoiceId);

        //get product obj and quantity from cart
        Map<Product, Integer> map = new HashMap<>();
        cartDAO.findCartItemByUserId(invoice.getUserId()).forEach(cartItemDTO -> {
            Product product = Product.builder()
                    .id(cartItemDTO.getProduct().getId())
                    .name(cartItemDTO.getProduct().getProductName())
                    .price(cartItemDTO.getProduct().getPrice())
                    .discount(cartItemDTO.getProduct().getDiscount()).build();
            map.put(product, cartItemDTO.getQuantity());
        });

        //delete cart items of user
        cartDAO.deleteAll(invoice.getUserId());

        //update available of products
        productDAO.updateAvailable(map);

        //set status for invoice
        boolean isSetStatus = invoiceStatusDAO.setStatus(invoiceId, SystemConstant.INVOICE_PROCESSING);
        mail.sendMailInvoice(invoice, map);
        return isTranferInvoiceItem && isSetStatus;
    }

    public boolean changeInvoiceStatus(Integer invoiceId, String selectedStatus) {
        return invoiceStatusDAO.changeStatus(invoiceId, selectedStatus);
    }

    public boolean saveInvoiceWithCookie(Invoice invoice, Cookie[] cookies) {
        Map<Product, Integer> map = new HashMap<>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("productId")) {
                int productId = Integer.parseInt(cookie.getName().substring("productId".length()));
                Product product = productDAO.findOneById(productId);
                int quantity = Integer.parseInt(cookie.getValue());
                map.put(product, quantity);
            }
        }
        int invoiceId = invoiceDAO.save(invoice);
        if (invoiceId == 0)
            return false;
        //save items
        boolean isSave = invoiceItemDAO.save(invoiceId, map);
        //update available of products
        productDAO.updateAvailable(map);
        //set status for invoice
        boolean isSetStatus = invoiceStatusDAO.setStatus(invoiceId, SystemConstant.INVOICE_PROCESSING);
        mail.sendMailInvoice(invoice, map);
        return isSave && isSetStatus;
    }
}
