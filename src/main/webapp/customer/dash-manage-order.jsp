<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<fmt:setLocale value="vi_VN"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Quản lý đơn hàng</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-manage-order.css"/>">
</head>

<body class="config">
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== App Content ======-->
    <div class="container-content mt-5">
        <div class="app-content">
            <div class="u-s-p-b-60">

                <div class="section__content">
                    <div class="dash">
                        <div class="container">
                            <div class="row">
                                <div class="col-lg-3 col-md-12">
                                    <!--====== Dashboard Features ======-->
                                    <%@ include file="/customer/common/nav_dash.jsp" %>
                                    <!--====== End - Dashboard Features ======-->
                                </div>
                                <div class="col-lg-9 col-md-12">
                                    <h1 class="dash__h1 u-s-m-b-30">CHI TIẾT ĐƠN HÀNG</h1>
                                    <div
                                            class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white u-s-m-b-30">
                                        <div class="dash__pad-2">
                                            <div class="dash-l-r">
                                                <div>
                                                    <div class="manage-o__text-2 u-c-secondary">ĐƠN HÀNG
                                                        #${requestScope.invoiceDTO.id}
                                                    </div>
                                                    <fmt:formatDate value="${requestScope.invoiceDTO.createdAt}"
                                                                    pattern=" 'Ngày' dd 'tháng' MM 'năm' yyyy hh:mma"
                                                                    var="formatedDate"/>
                                                    <div class="manage-o__text u-c-silver">${formatedDate}
                                                    </div>
                                                </div>
                                                <div>
                                                    <div class="manage-o__text-2 u-c-silver">Tổng:
                                                        <fmt:formatNumber var="formatedPrice1"
                                                                          value="${requestScope.invoiceDTO.total + requestScope.invoiceDTO.shippingFee}"
                                                                          type="currency"/>
                                                        <span class="manage-o__text-2 u-c-secondary">${formatedPrice1}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div
                                            class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white u-s-m-b-30">
                                        <div class="dash__pad-2">
                                            <div class="manage-o">

                                                <div class="dash-l-r">
                                                    <fmt:formatDate value="${requestScope.invoiceDTO.createdAt}"
                                                                    pattern=" 'ngày' dd 'tháng' MM 'năm' yyyy"
                                                                    var="formatedDate"/>
                                                    <div class="manage-o__text u-c-secondary">Vận chuyển ${formatedDate}
                                                    </div>
                                                    <c:if test="${invoiceDTO.paymentMethod.equals('COD') && invoiceDTO.status == SystemConstant.INVOICE_PROCESSING}">
                                                        <button data-input-id="${invoiceDTO.id}" class="btn-cancel"
                                                                style="color: #ffffff;
                                                        background-color: #ff4500;
                                                        border: none;
                                                        border-radius: 10px;
                                                        padding: 5px 15px;">
                                                            Hủy đơn hàng
                                                        </button>
                                                    </c:if>
                                                </div>
                                                <div class="manage-o__timeline">
                                                    <div class="timeline-row">
                                                        <c:forEach var="item" items="${states}">
                                                            <div class="col-lg-4 u-s-m-b-30">
                                                                <div class="timeline-step">
                                                                    <div class="timeline-l-i
                                                                    <c:if test="${item.getValue()==true}">
                                                                        timeline-l-i--finish">
                                                                        </c:if>
                                                                        <span class="timeline-circle"></span>
                                                                    </div>
                                                                    <span class="timeline-text">${item.getKey()}</span>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <c:forEach var="item" items="${requestScope.listInvoiceItemDTO}">
                                                    <div class="manage-o__description">
                                                        <div class="description__container">
                                                            <div class="description__img-wrap">

                                                                <img class="u-img-fluid"
                                                                     src="<c:url value="/images/products/${item.productImage}"/>"
                                                                     alt="">
                                                            </div>
                                                            <div class="description-title">${item.productName}
                                                            </div>
                                                        </div>
                                                        <div class="description__info-wrap">
                                                            <div>

                                                                <span class="manage-o__text-2 u-c-silver">Số lượng:

                                                                    <span
                                                                            class="manage-o__text-2 u-c-secondary">${item.quantity}</span></span>
                                                            </div>
                                                            <div>
                                                                <span class="manage-o__text-2 u-c-silver">Giá :
                                                                    <fmt:formatNumber var="formatedPrice"
                                                                                      value="${item.price}"
                                                                                      type="currency"/>
                                                                    <span class="manage-o__text-2 u-c-secondary">${formatedPrice}</span></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-lg-6">
                                            <div class="dash__box dash__box--bg-white dash__box--shadow u-h-100">
                                                <div class="dash__pad-3">
                                                    <h2 class="dash__h2 u-s-m-b-8"
                                                        style="font-weight: 600; padding-bottom: 5px">Địa chỉ giao
                                                        hàng</h2>
                                                    <span class="dash__text-2">${requestScope.invoiceDTO.fullName}</span>

                                                    <span class="dash__text-2">${requestScope.invoiceDTO.address}</span>

                                                    <span class="dash__text-2">${requestScope.invoiceDTO.phoneNumber}</span>
                                                </div>
                                            </div>

                                        </div>
                                        <div class="col-lg-6">
                                            <div class="dash__box dash__box--bg-white dash__box--shadow u-h-100">
                                                <div class="dash__pad-3">
                                                    <div class="dash-l-r u-s-m-b-8">
                                                        <fmt:formatNumber var="formatedPrice"
                                                                          value="${requestScope.invoiceDTO.total}"
                                                                          type="currency"/>
                                                        <div class="manage-o__text-2 u-c-secondary">Trị giá</div>
                                                        <div class="manage-o__text-2 u-c-secondary">${formatedPrice}</div>
                                                    </div>
                                                    <div class="dash-l-r u-s-m-b-8">
                                                        <fmt:formatNumber var="formatedPrice1"
                                                                          value="${requestScope.invoiceDTO.shippingFee}"
                                                                          type="currency"/>
                                                        <div class="manage-o__text-2 u-c-secondary">Phí vận chuyển
                                                        </div>
                                                        <div class="manage-o__text-2 u-c-secondary">${formatedPrice1}</div>
                                                    </div>
                                                    <div class="dash-l-r u-s-m-b-8">
                                                        <fmt:formatNumber var="formatedPrice2"
                                                                          value="${requestScope.invoiceDTO.total + requestScope.invoiceDTO.shippingFee}"
                                                                          type="currency"/>
                                                        <div class="manage-o__text-2 u-c-secondary">Tổng</div>
                                                        <div class="manage-o__text-2 u-c-secondary">${formatedPrice2}</div>
                                                    </div>
                                                    <c:if test="${invoiceDTO.paymentMethod.equals('COD')}">
                                                        <span class="dash__text-2">Thanh toán bằng tiền mặt khi giao hàng</span>
                                                    </c:if>
                                                    <c:if test="${invoiceDTO.paymentMethod.equals('VNPAY')}">
                                                        <span class="dash__text-2">Thanh toán bằng VNPAY</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="/customer/common/footer.jsp" %>
</div>
<!--====== End - Main App ======-->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $('.btn-cancel').on('click', function () {
        Swal.fire({
            title: "Hủy đơn hàng ?",
            text: "Bạn có muốn hủy đơn hàng này ?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            cancelButtonText: "Không",
            confirmButtonText: "Có"
        }).then((result) => {
            if (result.isConfirmed) {
                var inputData = $(this).data('input-id');
                // using Ajax to send data to server
                $.ajax({
                    type: "DELETE",
                    url: "<c:url value="/invoice-details"/>",
                    data: JSON.stringify(inputData),
                    contentType: "application/json; charset=utf-8",
                    success: function () {
                        Swal.fire({
                            icon: "success",
                            title: "Hủy đơn hàng thành công !",
                            toast: true,
                            position: "top-end",
                            showConfirmButton: false,
                            timer: 700,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });
                        setTimeout(function () {
                            window.location.href = "<c:url value="/invoice-details?id=${requestScope.invoiceDTO.id}"/>";
                        }, 700);
                    },
                    error: function () {
                        console.log('Thất bại !')
                        Swal.fire({
                            icon: "warning",
                            title: "Hủy thất bại!",
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
                });
            }
        })
    });
</script>
</body>

</html>