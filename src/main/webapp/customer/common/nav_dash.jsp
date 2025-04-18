<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="dash__box dash__box--bg-white dash__box--shadow u-s-m-b-30">
    <div class="dash__pad-1">

        <span class="dash__text u-s-m-b-16">Xin Chào, ${auth.fullName()}</span>
        <ul class="dash__f-list">
            <li>
                <a href="<c:url value="/profile"/>">Thông tin tài
                    khoản</a>
            </li>

            </li>
            <li>
                <a class="dash-active" href="<c:url value="/invoices"/>">Đơn đặt
                    hàng</a>
            </li>

        </ul>
    </div>
</div>
<div class="dash__box dash__box--bg-white dash__box--shadow dash__box--w">
    <div class="dash__pad-1">
        <ul class="dash__w-list">
            <li>
                <div class="dash__w-wrap">
                        <span class="dash__w-icon dash__w-icon-style-1"><i
                                class="fas fa-cart-arrow-down"></i></span>
                    <span class="dash__w-text">${totalItem}</span>
                    <span class="dash__w-name">Đơn Đặt Hàng</span>
                </div>
            </li>
            <li>
                <div class="dash__w-wrap">
                        <span class="dash__w-icon dash__w-icon-style-2"><i
                                class="fas fa-times"></i></span>
                    <span class="dash__w-text">${numInvoiceCanceled}</span>
                    <span class="dash__w-name">Đơn Hủy</span>
                </div>
            </li>
            <li>
                <div class="dash__w-wrap">
                        <span class="dash__w-icon dash__w-icon-style-3"><i
                                class="far fa-heart"></i></span>
                    <span class="dash__w-text">${numWishlistItems}</span>
                    <span class="dash__w-name">Yêu Thích</span>
                </div>
            </li>
        </ul>
    </div>
</div>
