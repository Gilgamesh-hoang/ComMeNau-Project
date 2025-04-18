<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="sidebar" class="sidebar">
    <ul class="nav nav-list d-flex justify-content-center">
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/products"/>"> <span
                class="menu-text <c:if test="${productActive != null}">active</c:if>">Quản lý sản phẩm </span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/blogs"/>"> <span
                class="menu-text <c:if test="${blogActive != null}">active</c:if>">Quản lý bài viết </span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/categories"/>"> <span
                class="menu-text <c:if test="${categoryActive != null}">active</c:if>">Quản lý thể loại </span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/user"/>"> <span
                class="menu-text <c:if test="${userActive != null}">active</c:if>  ">Quản lý khách hàng </span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/invoices"/>"> <span
                class="menu-text <c:if test="${invoiceActive != null}">active</c:if>">Quản lý đơn hàng </span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/contacts"/>"> <span
                class="menu-text <c:if test="${contactActive != null}">active</c:if>">Quản lý phản hồi</span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/chat"/>"> <span
                class="menu-text ">Hộp Thoại</span></a></li>
        <li class="flex-fill"><a class="d-flex align-items-center" href="<c:url value="/admin/cancel-products"/>"> <span
                class="menu-text <c:if test="${cancelActive != null}">active</c:if>">Sản phẩm hủy</span></a></li>
    </ul>
</div>