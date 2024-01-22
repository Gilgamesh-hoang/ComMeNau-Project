package com.commenau.dao;

import com.commenau.model.InvoiceStatus;
import com.commenau.connectionPool.ConnectionPool;
public class InvoiceStatusDAO {

    public InvoiceStatus getStatusByInvoice(int invoiceId) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select status, createdAt from invoice_status where invoiceId = ?")
                    .bind(0, invoiceId)
                    .mapToBean(InvoiceStatus.class)
                    .stream().findFirst().orElse(null);
        });
    }

    public boolean setStatus(int invoiceId, String status) {
        String sql = "INSERT INTO invoice_status (invoiceId, status) VALUES(:invoiceId, :status)";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }
    public boolean changeStatus(int invoiceId, String status) {
        String sql = "UPDATE invoice_status SET status = :status WHERE invoiceId = :invoiceId";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }
}
