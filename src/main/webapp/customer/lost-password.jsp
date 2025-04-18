<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quên mật khẩu</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/lost-password.css"/>">
</head>
<body>
<div id="app">
    <c:if test="${userNotExists != null}">
        <c:set var="alert" value="danger"/>
        <c:set var="msg" value="<strong>Đặt lại mật khẩu thất bại.</strong> Email không tồn tại trong hệ thống."/>
    </c:if>
    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <!--====== End - Main Header ======-->
    <!--====== App Content ======-->
    <div class="app-content p-b-60">
        <!--====== Section Intro ======-->
        <div class="section__intro m-b-30 m-t-50">
            <div class="container">
                <h1 class="section__heading ">QUÊN MẬT KHẨU</h1>
            </div>
        </div>
        <!--====== End - Section Intro ======-->
        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
                <div class="row row--center">
                    <div class="col-lg-6">
                        <div class="l-f-o">
                            <div class="l-f-o__pad-box">
                                <c:if test="${msg != null}">
                                    <div class="alert alert-${alert} alert-dismissible fade show" role="alert">
                                            ${msg}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <h1 class="gl-h1">Đặt lại mật khẩu</h1>

                                <span class="gl-text m-b-30">Nhập email của bạn vào bên dưới và chúng tôi
                                        sẽ gửi cho bạn đường dẫn để thiết lập lại mật khẩu.</span>
                                <form id="lostPassForm" class="l-f-o__form" action="<c:url value="/lost-password"/>" method="post">
                                    <div class="m-b-30">
                                        <label class="gl-label" >Email <span class="required-check">*</span></label>
                                        <input class="input-text input-text--primary-style" type="email"
                                               name="email" placeholder="Nhập email của bạn" data-rule="required|email">
                                    </div>
                                    <div class="m-b-30">

                                        <button class="btn btn--e-transparent-brand-b-2" type="submit">GỬI</button>
                                    </div>
                                    <div class="m-b-30">

                                        <a class="gl-link" href="<c:url value="/login"/>">Đăng nhập</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->

        <!--====== End - Section 2 ======-->
    </div>
    <!--====== End - App Content ======-->

    <!--====== Main Footer ======-->
    <%@ include file="/customer/common/footer.jsp" %>
</div>

<script src="<c:url value="/validate/validator.js"/>"></script>

<script>
    // Go to validation
    new Validator(document.querySelector('#lostPassForm'), function (err, res) {
        return res;
    });
</script>
</body>

</html>
