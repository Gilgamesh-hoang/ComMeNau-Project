package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;

public class InvoiceStatusDAO {

    public String getStatusByInvoice(int invoiceId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT status from invoice_status where invoiceId = ?")
                        .bind(0, invoiceId).mapTo(String.class).stream().findFirst().orElse(null)
        );
    }

    public int countInvoiceByStatus(String status, long userId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(iStatus.id) FROM invoice_status iStatus " +
                                "INNER JOIN invoices i ON i.id = iStatus.invoiceId " +
                                "WHERE iStatus.status = :status AND i.userId = :userId")
                        .bind("status", status).bind("userId", userId)
                        .mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public boolean setStatus(int invoiceId, String status) {
        String sql = "INSERT INTO invoice_status (invoiceId, status) VALUES(:invoiceId, :status)";
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }

    public boolean changeStatus(int invoiceId, String status) {
        String sql = "UPDATE invoice_status SET status = :status WHERE invoiceId = :invoiceId";
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql).bind("status", status).bind("invoiceId", invoiceId).execute());
        return result > 0;
    }
}
