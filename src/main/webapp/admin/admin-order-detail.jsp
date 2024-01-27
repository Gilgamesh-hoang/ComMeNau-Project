<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bài viết</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-order.css"/>">
</head>

<body class="no-skin">
<!-- navbar main-->
<%@ include file="/admin/common/header.jsp" %>
<!-- end navbar main-->
<div class="fix">
    <!-- navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- end navbar left-->

    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5 href="#">Quản lý đơn hàng</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content">
                <div class="content">
                    <div class="status">
                        <div class="date">
                                <span>
                                    <i class="fa-solid fa-calendar"></i>
                                    <b>
                                        <fmt:formatDate value="${invoice.createdAt}"
                                                        pattern="dd-MM-yyy hh:mma"/>
                                    </b>
                                </span>
                            <p>Mã đơn hàng : ${invoice.id}</p>
                        </div>
                        <div class="checkline">
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-user"
                                                       style="color: rgb(67, 142, 185)"></i></div>
                                <div class="status-content">
                                    <p><b>Khách hàng</b> <br>
                                        ${invoice.fullName}<br>
                                        ${invoice.email}
                                    </p>
                                </div>
                            </div>
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-truck"
                                                       style="color: rgb(67, 142, 185);"></i></div>
                                <div class="status-content">
                                    <p><b>Thông tin đơn hàng</b> <br>
                                        Phạm vi vận chuyển : Việt Nam <br>
                                        Phương thức thanh toán : ${invoice.paymentMethod}
                                    </p>
                                </div>
                            </div>
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-location-dot"
                                                       style="color: rgb(67, 142, 185);"></i>
                                </div>
                                <div class="status-content">
                                    <p><b>Vận chuyển tới</b><br>
                                        ${invoice.address}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="information">
                        <table style="width: 100%;">
                            <tr class="head">
                                <td style="width: 50%;">Sản phẩm</td>
                                <td style="width: 15%;">Giá</td>
                                <td style="width: 20%;">Số lượng</td>
                                <td class="f-right">Tổng</td>
                            </tr>
                            <c:forEach var="item" items="${listItems}">
                                <tr class="item">
                                    <td style="display: flex;">
                                        <img class="img-product" src="" alt="">
                                        <div class="product-name">
                                                ${item.productName}
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatNumber type="currency" value="${item.price}"/>
                                    </td>
                                    <td>
                                            ${item.quantity}
                                    </td>
                                    <td class="f-right">
                                        <fmt:formatNumber type="currency" value="${item.price * item.quantity}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="final">
                                <td></td>
                                <td></td>
                                <td>Thành tiền : <br>
                                    Phí giao hàng : <br>
                                    Tổng : <br>
                                    Trạng thái :
                                </td>
                                <td class="f-right">
                                    <fmt:formatNumber type="currency" value="${invoice.total}"/><br>
                                    <fmt:formatNumber type="currency" value="${invoice.shippingFee}"/><br>
                                    <span style="font-size: 15px;font-weight: 700;">
                                        <fmt:formatNumber type="currency"
                                        value="${invoice.total + invoice.shippingFee}"/>
                                    </span> <br>
                                    <span class="status" style="padding: 5px; background-color: antiquewhite; color: rgb(67, 142, 185);border-radius: 5px;">
                                        ${invoice.status}
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="operation">
                        <select name="" id="select">
                            <c:forEach var="item" items="${states}">
                                <option value="${item}" <c:if test="${item.equals(invoice.status)}"> selected </c:if> >${item}</option>
                            </c:forEach>
                        </select>
                        <button class="change-state" data-id="${invoice.id}" <c:if test="${invoice.status.equals('Đã hủy')}"> disabled</c:if>>Lưu thay đổi</button>
                    </div>
                </div>

                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>
<%@ include file="/admin/common/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $('.change-state').on('click', function () {
        var button = $(this);
        var invoiceId = button.data('id');
        var selectedStatus = $('#select').val();
        $.ajax({
            type: "POST",
            url: "<c:url value="/admin/invoiceDetail"/>",
            data: {invoiceId: invoiceId, selectedStatus: selectedStatus},
            success: function () {
                var statusSpan = button.closest('.operation').prev('.information').find('.final .status');
                statusSpan.text(selectedStatus);
                Swal.fire({
                    icon: "success",
                    title : "Đổi trạng thái thành công",
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.onmouseenter = Swal.stopTimer;
                        toast.onmouseleave = Swal.resumeTimer;
                    }
                });
            },
            error: function () {
                Swal.fire({
                    icon: "warning",
                    title: "Đổi trạng thái thất bại!",
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.onmouseenter = Swal.stopTimer;
                        toast.onmouseleave = Swal.resumeTimer;
                    }
                });
            }
        })
    })
</script>
</body>

</html>