<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


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
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-edit-profile.css"/>">
</head>

<body class="config">
<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->


    <!--====== App Content ======-->
    <div class="container-content mt-5">
        <div class="app-content">
            <!--====== Section 2 ======-->
            <div class="u-s-p-b-60">

                <!--====== Section Content ======-->
                <div class="section__content">
                    <div class="dash">
                        <div class="container">
                            <div class="row">
                                <div class="col-lg-3 col-md-12">

                                    <!--====== Dashboard Features ======-->
                                    <div class="dash__box dash__box--bg-white dash__box--shadow u-s-m-b-30">
                                        <div class="dash__pad-1">

                                            <span class="dash__text u-s-m-b-16">Xin Chào, ${auth.fullName()}</span>
                                            <ul class="dash__f-list">
                                                <li>
                                                    <a class="dash-active" href="<c:url value="/profile"/>">Thông tin
                                                        tài
                                                        khoản</a>
                                                </li>

                                                </li>
                                                <li>
                                                    <a href="<c:url value="/invoices"/>">Đơn đặt
                                                        hàng</a>
                                                </li>

                                            </ul>
                                        </div>
                                    </div>
                                    <!--====== End - Dashboard Features ======-->
                                </div>
                                <div class="col-lg-9 col-md-12">
                                    <div class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white">
                                        <div class="dash__pad-2">
                                            <h1 class="dash__h1 u-s-m-b-14">Chỉnh sửa Hồ sơ</h1>

                                            <span class="dash__text u-s-m-b-30">Có vẻ như bạn chưa cập nhật hồ sơ
                                                    của mình</span>
                                            <div class="dash__link dash__link--secondary u-s-m-b-30">

                                                <a data-modal="modal" data-modal-id="#dash-newsletter">Theo dõi bản
                                                    tin</a>
                                            </div>
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <form class="dash-edit-p" id="profileForm">
                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">

                                                                <label class="gl-label" for="reg-fname">Họ <span
                                                                        class="required-check">*</span></label>

                                                                <input class="input-text input-text--primary-style"
                                                                       data-rule="required"
                                                                       type="text" name="lastName" id="reg-fname"
                                                                       value="${auth.lastName}"
                                                                       placeholder="Họ">
                                                            </div>
                                                            <div class="u-s-m-b-30">

                                                                <label class="gl-label" for="reg-lname">Tên <span
                                                                        class="required-check">*</span></label>

                                                                <input class="input-text input-text--primary-style"
                                                                       data-rule="required"
                                                                       type="text" name="firstName"
                                                                       value="${auth.firstName}"
                                                                       placeholder="Tên">
                                                            </div>
                                                        </div>

                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">
                                                                <h2 class="dash__h2 u-s-m-b-8">Số điện thoại <span
                                                                        class="required-check">*</span></h2>

                                                                <%--                                                                <span class="dash__text">Vui lòng nhập điện thoại di--%>
                                                                <%--                                                                        động của bạn</span>--%>
                                                                <div class="u-s-m-b-30"
                                                                     style="margin-top: 10px; width: 100%;">
                                                                    <input
                                                                            class="input-text input-text--primary-style"
                                                                            data-rule="required|phone"
                                                                            type="text" name="phoneNumber"
                                                                            value="${auth.phoneNumber}"
                                                                            placeholder="Số điện thoại">
                                                                </div>
                                                            </div>
                                                            <div class="u-s-m-b-30">
                                                                <h2 class="dash__h2 u-s-m-b-8">Địa chỉ <span
                                                                        class="required-check">*</span></h2>

                                                                <%--                                                                <span class="dash__text">Vui lòng nhập địa chỉ của--%>
                                                                <%--                                                                        bạn của bạn</span>--%>
                                                                <div class="u-s-m-b-30"
                                                                     style="margin-top: 10px; width: 100%;">
                                                                    <input
                                                                            class="input-text input-text--primary-style"
                                                                            data-rule="required"
                                                                            type="text" name="address" id="reg-lname"
                                                                            value="${auth.address}"
                                                                            placeholder="Địa chỉ">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <button class="btn--e-brand-b-2 btn-submit" type="submit">LƯU</button>
                                                        <input type="hidden" name="id" value="${auth.id}">
                                                        <c:if test="${requestScope.enoughError!=null}">
                                                            <b class="text-danger">${requestScope.enoughError}</b>
                                                        </c:if>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--====== End - Section Content ======-->
            </div>
            <!--====== End - Section 2 ======-->
        </div>
    </div>
    <!--====== End - App Content ======-->


    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script>
    let validate = false;
    // Go to validation
    new Validator(document.querySelector('#profileForm'), function (err, res) {
        validate = res;
    });


    $('#profileForm').on('submit', function (event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định của form

        // Kiểm tra xác thực trước khi gửi AJAX request
        if (validate === true) {
            var id = $('input[name="id"]').val();
            var lastName = $('input[name="lastName"]').val();
            var firstName = $('input[name="firstName"]').val();
            var phoneNumber = $('input[name="phoneNumber"]').val();
            var address = $('input[name="address"]').val();
            var formData = {
                id: id,
                lastName: lastName,
                firstName: firstName,
                phoneNumber: phoneNumber,
                address: address
            };

            // Gửi AJAX request
            $.ajax({
                type: 'POST',
                url: "<c:url value="/change-profile"/>",
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function () {
                    Swal.fire({
                        icon: "success",
                        title: "Thay đổi thành công",
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
                        window.location.href = "<c:url value="/profile"/>";
                    }, 700);

                },
                error: function () {
                    Swal.fire({
                        icon: "warning",
                        title: "Thay đổi không thành công",
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
        } else {
            // Xử lý khi form chưa được xác thực
            console.log("form error")
        }
    });
</script>
</body>

</html>