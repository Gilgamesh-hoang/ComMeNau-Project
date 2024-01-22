<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Thanh Toán</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/input.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/checkout.css"/>">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
          integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"
            integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body>
<fmt:setLocale value="vi_VN"/>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<div class="container-content mt-5">
    <form class="checkout-f__delivery" id="checkout-form">
        <div class="checkout-f d-flex flex-row">
            <div class="col-6 me-5">
                <h1 class="checkout-f__h1">THÔNG TIN VẬN CHUYỂN</h1>

                <div class="flex-fill">
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Họ tên <span class="required-check">*</span></label>
                        <input data-rule="required|containsAllWhitespace" class="input-text input-text--primary-style"
                               type="text" name="fullName" value="${fullName}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Số điện thoại <span class="required-check">*</span></label>
                        <input data-rule="required|phone" class="input-text input-text--primary-style" type="text"
                               name="phoneNumber" value="${phoneNumber}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Email<span class="required-check">*</span></label>
                        <input data-rule="required|email" class="input-text input-text--primary-style" type="email"
                               name="email" value="${email}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Địa chỉ <span class="required-check">*</span></label>
                        <input data-rule="required|containsAllWhitespace" class="input-text input-text--primary-style"
                               type="text" name="address" value="${address}">
                    </div>
                    <div class="u-s-m-b-10">
                        <label class="gl-label">Ghi chú</label><textarea
                            class="text-area text-area--primary-style" name="note"></textarea>
                    </div>
                </div>

            </div>
            <div class="col-6">
                <h1 class="checkout-f__h1">SƠ LƯỢC ĐƠN HÀNG</h1>

                <div class="o-summary">
                    <div class="o-summary__section u-s-m-b-30">
                        <div class="o-summary__item-wrap gl-scroll">
                            <c:forEach var="item" items="${cart}">
                                <div class="o-card">
                                    <div class="o-card__flex">
                                        <div class="o-card__img-wrap">
                                            <img class="u-img-fluid"
                                                 src="<c:url value="/images/products/${item.product.images.get(0)}"/>"
                                                 alt="">
                                        </div>
                                        <div class="o-card__info-wrap">
                                            <span class="o-card__name"><a
                                                    href="#">${item.product.productName}</a></span>
                                            <span class="o-card__quantity">Số lượng x ${item.quantity}</span>
                                            <span class="o-card__price">
                                                <fmt:formatNumber value="${RoundUtil.roundPrice(item.product.price * (1 - item.product.discount))}"
                                                                  type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="o-summary__section u-s-m-b-30">
                        <div class="o-summary__box">
                            <table class="o-summary__table">
                                <tbody>
                                <tr>
                                    <td>Phí vận chuyển</td>
                                    <td><fmt:formatNumber value="0"
                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/></td>
                                </tr>
                                <tr>
                                    <td>Tổng giá thành</td>
                                    <td>
                                        <fmt:formatNumber value="${totalPrice}"
                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Tổng đơn hàng</td>
                                    <td>
                                        <fmt:formatNumber value="${totalPrice}"
                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="o-summary__section u-s-m-b-30">
                        <div class="o-summary__box">
                            <h1 class="checkout-f__h1">THÔNG TIN THANH TOÁN</h1>
                            <div class="u-s-m-b-10 d-flex flex-column" style="margin-top: 20px;">
                                <!--====== Radio Box ======-->
                                <div class="radio-box">
                                    <input type="radio" id="cod" name="paymentMethod" value="COD" checked>
                                    <div class="radio-box__state radio-box__state--primary">
                                        <label class="radio-box__label" for="cod">Thanh toán bằng tiền
                                            mặt
                                        </label>
                                    </div>
                                </div>
                                <!--====== End - Radio Box ======-->
                            </div>
                            <div class="u-s-m-b-10 d-flex flex-column" style="margin-top: 20px;">

                                <!--====== Radio Box ======-->
                                <div class="radio-box">
                                    <input type="radio" id="vnpay" name="paymentMethod" value="VNPAY">
                                    <div class="radio-box__state radio-box__state--primary">
                                        <label class="radio-box__label" for="vnpay">Thanh toán bằng
                                            VNPAY</label>
                                    </div>
                                </div>
                                <!--====== End - Radio Box ======-->
                            </div>
                            <div>
                                <button class="btn btn--e-brand-b-2" id="btn-checkout" type="submit">THANH TOÁN</button>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="amount" value="${totalPrice}">
                </div>
                <!--====== End - Order Summary ======-->
            </div>
        </div>
    </form>
</div>
<!--====== End - App Content 0347987462======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        let validate = false;
        // Go to validation
        new Validator(document.querySelector('#checkout-form'), function (err, res) {
            validate = res;
            console.log(res)
        });


        $('#checkout-form').on('submit', async function (event) {
            event.preventDefault();
            if (validate === true) {
                var formData = $('#checkout-form').serialize();
                var isVnPaySelected = $('#vnpay').is(':checked');
                var paymentUrl = isVnPaySelected ? '<c:url value="/vnpay-payment"/>' : '<c:url value="/payments"/>';

                var ajaxConfig = {
                    type: 'POST',
                    url: paymentUrl,
                    dataType: 'json',
                    data: formData
                };

                try {
                    var response = await $.ajax(ajaxConfig);
                    if (isVnPaySelected) {
                        handlePaymentResponse(response);
                    } else {
                        handlePaymentMessage("success", "Thanh toán thành công", '<c:url value="/carts"/>');
                    }
                } catch (error) {
                    handlePaymentMessage("warning", "Thanh toán thất bại!", null);
                }
            } else {
                console.log("form error")
            }
        });

        function handlePaymentResponse(response) {
            if (response.code === "00") {
                window.location.href = response.data;
            } else {
                handlePaymentMessage("warning", "Thanh toán thất bại!", null);
            }
        }

        function handlePaymentMessage(icon, message, url) {
            Swal.fire({
                icon: icon,
                title: message,
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 600,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                }
            });

            if (url !== null && icon === "success") {
                setTimeout(function () {
                    window.location.href = url;
                }, 700);
            }
        }

        <%--$('#checkout-form').on('submit', function (event) {--%>
        <%--    event.preventDefault();--%>
        <%--    if (validate === true) {--%>
        <%--        var formData = $('#checkout-form').serialize(); // Lấy dữ liệu từ form--%>
        <%--        // console.log((formData));--%>
        <%--        var isVnPaySelected = $('#vnpay').is(':checked');--%>
        <%--        if (isVnPaySelected) {--%>
        <%--            //vnpay--%>
        <%--            $.ajax({--%>
        <%--                type: 'POST',--%>
        <%--                url: '<c:url value="/vnpay"/>',--%>
        <%--                dataType: 'json',--%>
        <%--                data: formData,--%>
        <%--                success: function (response) {--%>
        <%--                    if (response.code === "00") {--%>
        <%--                        // Chuyển hướng người dùng đến trang thanh toán của VnPay--%>
        <%--                        // console.log(response.data);--%>
        <%--                        window.location.href = response.data;--%>
        <%--                    } else {--%>
        <%--                        // Xử lý khi có lỗi từ VnPay--%>
        <%--                        Swal.fire({--%>
        <%--                            icon: "warning",--%>
        <%--                            title: "Có lỗi khi tạo yêu cầu thanh toán",--%>
        <%--                            text: response.message,--%>
        <%--                            toast: true,--%>
        <%--                            position: "top-end",--%>
        <%--                            showConfirmButton: false,--%>
        <%--                            timer: 600,--%>
        <%--                            timerProgressBar: true,--%>
        <%--                            didOpen: (toast) => {--%>
        <%--                                toast.onmouseenter = Swal.stopTimer;--%>
        <%--                                toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                            }--%>
        <%--                        });--%>
        <%--                    }--%>
        <%--                },--%>
        <%--                error: function () {--%>
        <%--                    // Xử lý khi có lỗi từ VnPay--%>
        <%--                    Swal.fire({--%>
        <%--                        icon: "warning",--%>
        <%--                        title: "Có lỗi khi tạo yêu cầu thanh toán",--%>
        <%--                        toast: true,--%>
        <%--                        position: "top-end",--%>
        <%--                        showConfirmButton: false,--%>
        <%--                        timer: 600,--%>
        <%--                        timerProgressBar: true,--%>
        <%--                        didOpen: (toast) => {--%>
        <%--                            toast.onmouseenter = Swal.stopTimer;--%>
        <%--                            toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                        }--%>
        <%--                    });--%>
        <%--                }--%>
        <%--            });--%>


        <%--        }else {--%>
        <%--            // cod--%>
        <%--            $.ajax({--%>
        <%--                type: 'POST',--%>
        <%--                url: '<c:url value="/thanh-toan"/>',--%>
        <%--                dataType: 'json',--%>
        <%--                data: formData,--%>
        <%--                success: function () {--%>
        <%--                    Swal.fire({--%>
        <%--                        icon: "success",--%>
        <%--                        title: "Thanh toán thành công",--%>
        <%--                        toast: true,--%>
        <%--                        position: "top-end",--%>
        <%--                        showConfirmButton: false,--%>
        <%--                        timer: 600,--%>
        <%--                        timerProgressBar: true,--%>
        <%--                        didOpen: (toast) => {--%>
        <%--                            toast.onmouseenter = Swal.stopTimer;--%>
        <%--                            toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                        }--%>
        <%--                    });--%>
        <%--                    setTimeout(function () {--%>
        <%--                        window.location.href = "<c:url value="/gio-hang"/>";--%>
        <%--                    }, 700);--%>
        <%--                },--%>
        <%--                error: function () {--%>
        <%--                    Swal.fire({--%>
        <%--                        icon: "warning",--%>
        <%--                        title: "Thanh toán thất bại!",--%>
        <%--                        toast: true,--%>
        <%--                        position: "top-end",--%>
        <%--                        showConfirmButton: false,--%>
        <%--                        timer: 600,--%>
        <%--                        timerProgressBar: true,--%>
        <%--                        didOpen: (toast) => {--%>
        <%--                            toast.onmouseenter = Swal.stopTimer;--%>
        <%--                            toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                        }--%>
        <%--                    });--%>
        <%--                }--%>
        <%--            })--%>
        <%--        }--%>

        <%--    } else {--%>
        <%--        // Xử lý khi form chưa được xác thực--%>
        <%--        console.log("form error")--%>
        <%--    }--%>

        <%--});--%>
    });
</script>
</body>
</html>