package com.commenau.service;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.*;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.mail.MailService;
import com.commenau.mapper.InvoiceItemMapper;
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
    @Inject
    private InvoiceItemMapper itemMapper;

    public List<InvoiceItemDTO> getItemByInvoiceId(int invoiceId) {
        return invoiceItemDAO.findByInvoiceId(invoiceId).stream().map(item -> {
            InvoiceItemDTO dto = itemMapper.toDTO(item, InvoiceItemDTO.class);
            dto.setProductImage(productImageDAO.findAvatarByProductId(item.getProductId()));
            dto.setProductName(productDAO.findOneById(item.getProductId()).getName());
            return dto;
        }).collect(Collectors.toList());
    }

    public int countInvoiceByStatus(String status, long userId) {
        return invoiceStatusDAO.countInvoiceByStatus(status, userId);
    }

    public InvoiceDTO getInvoiceById(int invoiceId) {
        InvoiceDTO result = invoiceMapper.toDTO(invoiceDAO.getInvoiceById(invoiceId), InvoiceDTO.class);
        result.setTotal(calculateTotalPrice(invoiceId) + result.getShippingFee());
        result.setStatus(invoiceStatusDAO.getStatusByInvoice(invoiceId));
        return result;
    }

    private int calculateTotalPrice(int invoiceId) {
        int total = 0;
        for (InvoiceItem invoiceItem : invoiceItemDAO.findByInvoiceId(invoiceId)) {
            total += invoiceItem.getPrice() * invoiceItem.getQuantity();
        }
        return total;
    }
    public List<InvoiceDTO> getAllInvoice(PageRequest pageRequest) {
        return invoiceDAO.getAllInvoice(pageRequest)
                .stream()
                .map(this::mapToInvoiceDTO)
                .collect(Collectors.toList());
    }

    public List<InvoiceDTO> getAllInvoice(PageRequest pageRequest, long userId) {
        return invoiceDAO.getAllInvoice(pageRequest, userId)
                .stream()
                .map(this::mapToInvoiceDTO)
                .collect(Collectors.toList());
    }

    private InvoiceDTO mapToInvoiceDTO(Invoice invoice) {
        InvoiceDTO dto = invoiceMapper.toDTO(invoice, InvoiceDTO.class);
        dto.setStatus(invoiceStatusDAO.getStatusByInvoice(invoice.getId()));
        dto.setTotal(calculateTotalPrice(invoice.getId()) + dto.getShippingFee());
        return dto;
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

    public boolean changeStatus(Integer invoiceId, String selectedStatus) {
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


    public int countAll() {
        return invoiceDAO.countAll();
    }
}
