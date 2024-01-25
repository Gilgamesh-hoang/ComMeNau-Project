package com.commenau.service;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.*;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.mail.MailService;
import com.commenau.mapper.InvoiceMapper;
import com.commenau.model.Invoice;
import com.commenau.model.InvoiceItem;
import com.commenau.model.Product;
import com.commenau.pagination.PageRequest;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.*;
import java.util.stream.Collectors;

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
    private CartDAO cartDAO;
    @Inject
    private MailService mail;
    @Inject
    private InvoiceMapper invoiceMapper;
    public List<InvoiceDTO> getAllInvoiceDTOById(Long userId) {
        List<InvoiceDTO> result = new ArrayList<>();
        for (Invoice invoice : invoiceDAO.getAllInvoiceById(userId)) {
            double total = 0;
            double shippingFee = (invoiceDAO.getInvoiceById(invoice.getId()).getShippingFee() == null) ? 0 : invoiceDAO.getInvoiceById(invoice.getId()).getShippingFee();
            for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(invoice.getId())) {
                total += invoiceItem.getPrice() * invoiceItem.getQuantity();
            }
            InvoiceDTO invoicedto = InvoiceDTO.builder()
                    .id(invoice.getId())
                    .createdAt(invoice.getCreatedAt())
                    .status(invoiceStatusDAO.getStatusByInvoice(invoice.getId()))
//                    .total(total + shippingFee)
                    .build();
            result.add(invoicedto);
        }
        return result;
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

    public int countInvoiceByStatus(String status, long userId) {
        return invoiceStatusDAO.countInvoiceByStatus(status, userId);
    }
    public InvoiceDTO getInvoiceDTOById(int invoiceId) {
        double total = 0;
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        double shippingFee = invoice.getShippingFee() == null ? 0 : invoice.getShippingFee();
        for (InvoiceItem invoiceItem : invoiceItemDAO.getAllInvoiceItemById(invoiceId)) {
            total += invoiceItem.getPrice() * invoiceItem.getQuantity();
        }
        return InvoiceDTO.builder().id(invoiceId)
                .fullName(invoice.getFullName())
                .email(invoice.getEmail())
                .status(invoiceStatusDAO.getStatusByInvoice(invoiceId))
                .createdAt(invoice.getCreatedAt())
                .shippingFee(shippingFee)
//                .total(total)
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
                    .fullName(i.getFullName())
//                    .total(total)
                    .shippingFee(shippingFee)
                    .phoneNumber(i.getPhoneNumber())
                    .status(invoiceStatusDAO.getStatusByInvoice(i.getId()))
                    .build());
        }
        return re;
    }

    public List<Invoice> getAllInvoice() {
        return invoiceDAO.getAllInvoice();
    }

    public List<InvoiceDTO> getAllInvoice(PageRequest pageRequest, long userId) {
        return invoiceDAO.getAllInvoice(pageRequest, userId).stream().map(invoice -> {
            InvoiceDTO dto = invoiceMapper.toDTO(invoice, InvoiceDTO.class);
            dto.setStatus(invoiceStatusDAO.getStatusByInvoice(invoice.getId()));
            dto.setTotal((int) (invoiceItemDAO.totalPrice(invoice.getId()) +  dto.getShippingFee()));
            return dto;
        }).collect(Collectors.toList());

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
            Product product = productDAO.findOneById(cartItemDTO.getProductId());
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

    public int countAll(long userId) {
        return invoiceDAO.countAll(userId);
    }


}
