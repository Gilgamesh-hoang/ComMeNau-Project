<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Giỏ hàng</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/cart.css"/>">
</head>
<body>
<fmt:setLocale value="vi_VN"/>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--====== End - Main Header ======-->
<!--====== App Content ======-->
<div class="app-content">
    <!--====== Section 2 ======-->
    <div class="u-s-p-b-60">

        <!--====== Section Intro ======-->
        <div class="section__intro u-s-m-b-60">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section__text-wrap">
                            <h1 class="section__heading u-c-secondary">GIỎ HÀNG</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Intro ======-->


        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 u-s-m-b-30">
                        <div class="table-responsive">
                            <table class="table-p">
                                <tbody>
                                <c:forEach var="item" items="${cart}">
                                    <tr>
                                        <td>
                                            <div class="table-p__box">
                                                <div class="table-p__img-wrap">
                                                    <img class="u-img-fluid"
                                                         src="<c:url value="/images/products/${item.product.avatar}"/>" alt="">
                                                </div>
                                                <div class="table-p__info">
                                                    <span class="table-p__name"><a
                                                            href="<c:url value="/product/${item.product.id}"/>">${item.product.name}</a></span>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="table-p__price">
                                                <fmt:formatNumber value="${RoundUtil.roundPrice(item.product.price * (1 - item.product.discount)) * item.quantity}"
                                                                  type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="table-p__input-counter-wrap">
                                                <!--====== Input Counter ======-->
                                                <div class="input-counter">
                                                    <span data-input-id="${item.product.id}"
                                                          class="input-counter__minus fas fa-minus"></span>
                                                    <input data-input-id="${item.product.id}"
                                                           class="input-counter__text input-counter--text-primary-style"
                                                           type="text" value="${item.quantity}" data-min="1"
                                                           data-max="${item.product.available}">
<%--                                                    <input id="counter-input" data-input-id="${item.product.id}"--%>
<%--                                                           class="input-counter__text input-counter--text-primary-style"--%>
<%--                                                           type="text" value="${item.quantity}" data-min="1"--%>
<%--                                                           data-max="${item.product.available}">--%>
                                                    <span data-input-id="${item.product.id}"
                                                          class="input-counter__plus fas fa-plus"></span>
                                                </div>
                                                <!--====== End - Input Counter ======-->
                                            </div>
                                        </td>
                                        <td>
                                            <div class="table-p__del-wrap">
                                                <button class="far fa-trash-alt table-p__delete-link btn-delete"
                                                        data-input-id="${item.product.id}"></button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-lg-12">
                        <div class="route-box">
                            <div class="route-box__g1">

                                <a class="route-box__link" href="<c:url value="${sessionScope.get(SystemConstant.PRE_PAGE)}"/>"><i
                                        class="fas fa-long-arrow-alt-left"></i>
                                    <span>TIẾP TỤC MUA SẮM</span></a>
                            </div>
                            <div class="route-box__g2">
                                <button class="route-box__link btn-delete__all">
                                    <i class="fas fa-trash"></i><span>DỌN SẠCH GIỎ HÀNG</span></button>
                                <button class="route-box__link" id="update-cart-btn"><i class="fas fa-sync"></i>
                                    <span>CẬP NHẬT GIỎ HÀNG</span></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="u-s-p-b-60">
        <div class="section__content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 u-s-m-b-30">
                        <form class="f-cart">
                            <div class="row">
                                <div class="col-lg-4 col-md-6 u-s-m-b-30">
                                    <div class="f-cart__pad-box">
                                        <div class="u-s-m-b-30">
                                            <table class="f-cart__table">
                                                <tbody>
                                                <tr>
                                                    <td>TỔNG CỘNG</td>
                                                    <td>
                                                        <fmt:formatNumber value="${RoundUtil.roundPrice(totalPrice)}"
                                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>PHÍ VẬN CHUYỂN</td>
                                                    <td><fmt:formatNumber value="0" type="currency" maxFractionDigits="0" currencyCode="VND"/></td>
                                                </tr>
                                                <tr>
                                                    <td>THÀNH TIỀN</td>
                                                    <td>
                                                        <fmt:formatNumber value="${RoundUtil.roundPrice(totalPrice)}"
                                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div>
                                            <a href="<c:url value="/payments"/>" class="btn btn--e-brand-b-2" type="submit"> Thanh toán</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        $('.input-counter__text').on('input', function () {
            var value = parseInt($(this).val());
            var min = parseInt($(this).attr('data-min'));
            var max = parseInt($(this).attr('data-max'));
            if (isNaN(value) || value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }

            $(this).val(value);
        });

        //Handle the click event of the increase button
        $('.input-counter__plus').on('click', function () {
            var inputId = $(this).attr('data-input-id');
            var inputElement = $('.input-counter__text[data-input-id="' + inputId + '"]');
            var max = parseInt($(inputElement).attr('data-max'));
            if (inputElement.length > 0) {
                var newValue = parseInt(inputElement.val()) + 1;
                if (newValue <= max)
                    inputElement.val(newValue);
            }
        });

        // Handle the click event of the decrease button
        $('.input-counter__minus').on('click', function () {
            var inputId = $(this).attr('data-input-id');
            var inputElement = $('.input-counter__text[data-input-id="' + inputId + '"]');
            var min = parseInt($(inputElement).attr('data-min'));
            if (inputElement.length > 0) {
                var newValue = parseInt(inputElement.val()) - 1;
                if (newValue >= min)
                    inputElement.val(newValue);
            }
        });


        // Handle the click event of the update cart button
        $('#update-cart-btn').on('click', function () {
            var inputData = {}; // key-value

            // loop thought all the input tags in table and save value, data-input-id to the obj
            $('.table-p input').each(function () {
                var inputValue = $(this).val();
                var inputId = $(this).data('input-id');
                inputData[inputId] = inputValue; // save with key is data-input-id
            });

            console.log(inputData);
            // using Ajax to send data to server
            $.ajax({
                type: "PUT",
                url: "<c:url value="/carts"/>",
                data: JSON.stringify(inputData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                    Swal.fire({
                        icon: "success",
                        title: "Cập nhật thành công",
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
                        window.location.href = "<c:url value="/carts"/>";
                    }, 750);
                },
                error: function (error) {
                    <%--console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);--%>
                    <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                    Swal.fire({
                        icon: "warning",
                        title: "Cập nhật giỏ hàng thất bại",
                        toast: true,
                        position: "top-end",
                        showConfirmButton: false,
                        timer: 500,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                            toast.onmouseenter = Swal.stopTimer;
                            toast.onmouseleave = Swal.resumeTimer;
                        }
                    });
                    setTimeout(function () {
                        window.location.href = "<c:url value="/carts"/>";
                    }, 600);
                }
            });
        });

        $('.btn-delete').on('click', function () {
            Swal.fire({
                title: "Xóa khỏi giỏ hàng?",
                text: "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var cartItemId = $(this).data('input-id');
                    console.log(cartItemId);
                    // using Ajax to send data to server
                    $.ajax({
                        type: "DELETE",
                        url: "<c:url value="/carts"/>",
                        data: JSON.stringify(cartItemId),
                        contentType: "application/json; charset=utf-8",
                        success: function () {
                            Swal.fire({
                                icon: "success",
                                title: "Xóa sản phẩm thành công",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            setTimeout(function () {
                                window.location.href = "<c:url value="/carts"/>";
                            }, 600);
                        },
                        error: function () {
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa sản phẩm thất bại",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                        }
                    });

                }
            });
        });

        $('.btn-delete__all').on('click', function () {
            Swal.fire({
                title: "Xóa tất cả?",
                text: "Bạn có chắc muốn xóa tất cả sản phẩm khỏi giỏ hàng!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    // using Ajax to send data to server
                    $.ajax({
                        type: "DELETE",
                        url: "<c:url value="/carts"/>",
                        data: JSON.stringify("-1"),
                        contentType: "application/json; charset=utf-8",
                        success: function () {
                            Swal.fire({
                                icon: "success",
                                title: "Đã xóa tất cả sản phẩm",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            setTimeout(function () {
                                window.location.href = "<c:url value="/carts"/>";
                            }, 600);
                        },
                        error: function () {
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa sản phẩm thất bại",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                        }
                    });

                }
            });
        });
    });

</script>
</body>
</html>