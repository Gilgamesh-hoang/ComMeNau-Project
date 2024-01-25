package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.constant.SystemConstant;
import com.commenau.model.Invoice;
import com.commenau.pagination.PageRequest;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.sql.Types;
import java.util.List;

public class InvoiceDAO {

    public List<Invoice> getAllInvoiceById(Long userId) {
        List<Invoice> invoices = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("SELECT id FROM invoices WHERE userId = ?")
                    .bind(0, userId)
                    .mapToBean(Invoice.class)
                    .list();
        });
        return invoices;
    }

    public List<Invoice> get10InvoiceById(Long userId) {
        List<Invoice> invoices = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("SELECT id FROM invoices WHERE userId = ? ORDER BY createdAt DESC LIMIT 10 ")
                    .bind(0, userId)
                    .mapToBean(Invoice.class)
                    .list();
        });
        return invoices;
    }

    public Invoice getInvoiceById(int invoiceId) {
        return JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select userId,fullName,email ,shippingFee,address,phoneNumber,paymentMethod,createdAt from invoices where id = ?")
                    .bind(0, invoiceId)
                    .mapToBean(Invoice.class)
                    .first();
        });
    }

    public List<Invoice> getAllInvoicePaged(int nextPage, int pageSize) {
        return JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select i.* from invoices i JOIN invoice_status s ON i.id = s.invoiceId ORDER BY s.status DESC ,i.createdAt desc limit ? offset ?")
                    .bind(0, pageSize)
                    .bind(1, (nextPage - 1) * pageSize)
                    .mapToBean(Invoice.class)
                    .list();
        });
    }

    public List<Invoice> getAllInvoice(PageRequest pageRequest, long userId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM invoices WHERE userId = :userId ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("userId", userId).mapToBean(Invoice.class).stream().toList()
        );
    }

    public int countAll(long userId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM invoices WHERE userId = :userId")
                        .bind("userId", userId).mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }


    public List<Invoice> getAllInvoice() {
        return JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select id from invoices")
                    .mapToBean(Invoice.class)
                    .list();
        });
    }

    public Integer sellingOfDay() {
        String sql = "SELECT COUNT(i.id) FROM invoices i INNER JOIN invoice_status s ON i.id = s.invoiceId " +
                "WHERE DATE(i.createdAt) = CURDATE() AND s.status = '" + SystemConstant.INVOICE_DELIVERED + "'";
        return calculate(sql, Integer.class);
    }

    public Integer sellingOfMonth() {
        String sql = "SELECT COUNT(i.id) FROM invoices i INNER JOIN invoice_status s ON i.id = s.invoiceId " +
                "WHERE MONTH(i.createdAt) = MONTH(CURDATE()) AND s.status = '" + SystemConstant.INVOICE_DELIVERED + "'";
        return calculate(sql, Integer.class);
    }

    public Double revenueOfMonth() {
        String sql = "SELECT SUM(ii.price * ii.quantity) FROM invoices i " +
                "INNER JOIN invoice_items ii ON i.id = ii.invoiceId " +
                "INNER JOIN invoice_status ist ON i.id = ist.invoiceId " +
                "WHERE ist.status = '" + SystemConstant.INVOICE_DELIVERED + "'" +
                "AND YEAR(i.createdAt) = YEAR(CURDATE()) " +
                "AND MONTH(i.createdAt) = MONTH(CURDATE()) ";
        return calculate(sql, Double.class);
    }

    private <T> T calculate(String sql, Class<T> tClass) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(tClass)
                        .findFirst()
                        .orElse(null)
        );
    }

    public Double revenueOfDay() {
        String sql = "SELECT SUM(ii.price * ii.quantity) FROM invoices i " +
                "INNER JOIN invoice_items ii ON i.id = ii.invoiceId " +
                "INNER JOIN invoice_status ist ON i.id = ist.invoiceId " +
                "WHERE ist.status = '" + SystemConstant.INVOICE_DELIVERED + "'" +
                "AND YEAR(i.createdAt) = YEAR(CURDATE()) " +
                "AND MONTH(i.createdAt) = MONTH(CURDATE()) " +
                "AND DATE(i.createdAt) = CURDATE()";
        return calculate(sql, Double.class);
    }

    public int save(Invoice invoice) {
        String sql = "INSERT INTO invoices(userId,fullName,shippingFee,note,address,phoneNumber,email, paymentMethod) VALUES " +
                "(:userId,:fullName,:shippingFee,:note,:address,:phoneNumber,:email, :paymentMethod)";
        int invoceId = JDBIConnector.getInstance().inTransaction(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("fullName", invoice.getFullName())
                    .bind("shippingFee", invoice.getShippingFee())
                    .bind("note", invoice.getNote())
                    .bind("address", invoice.getAddress())
                    .bind("phoneNumber", invoice.getPhoneNumber())
                    .bind("email", invoice.getEmail())
                    .bind("paymentMethod", invoice.getPaymentMethod());
            if (invoice.getUserId() != null)
                update.bind("userId", invoice.getUserId());
            else update.bindNull("userId", Types.INTEGER);
            return update.executeAndReturnGeneratedKeys("id").mapTo(Integer.class).first();
        });
        return invoceId;
    }


}
