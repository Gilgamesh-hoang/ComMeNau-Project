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

    public Invoice getInvoiceById(int invoiceId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM invoices where id = ?")
                        .bind(0, invoiceId).mapToBean(Invoice.class).stream().findFirst().orElse(null)
        );
    }


    public int countAll(long userId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM invoices WHERE userId = :userId")
                        .bind("userId", userId).mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public List<Invoice> getAllInvoice(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM invoices ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).mapToBean(Invoice.class).stream().toList()
        );
    }

    public List<Invoice> getAllInvoice(PageRequest pageRequest, long userId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM invoices WHERE userId = :userId ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("userId", userId).mapToBean(Invoice.class).stream().toList()
        );
    }

    public int countAll() {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM invoices")
                        .mapTo(Integer.class).stream().findFirst().orElse(0)
        );
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
