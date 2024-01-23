<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng nhập</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/signin.css"/>">

</head>
<body>
<div id="app">
    <c:if test="${registerSuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Bạn đã đăng kí thành công.</strong> Vui lòng kiểm tra email để kích hoạt tài khoản."/>
    </c:if>
    <c:if test="${verifySuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Xác minh tài khoản thành công.</strong> Tài khoản của bạn đã được xác minh, đăng nhập đã sử dụng hệ thống."/>
    </c:if>
    <c:if test="${verifyError != null}">
        <c:set var="alert" value="danger"/>
        <c:set var="msg"
               value="<strong>Xác minh tài khoản thất bại.</strong> Vui lòng kiểm tra lại email để xác minh tài khoản."/>
    </c:if>
    <c:if test="${signinError != null}">
        <c:set var="alert" value="danger"/>
        <c:set var="msg" value="<strong>Bạn đã đăng nhập thất bại.</strong> Vui lòng kiểm tra username hoặc password."/>
    </c:if>
    <c:if test="${resetPasswordSuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Đặt lại mật khẩu thành công.</strong> Vui lòng kiểm tra email để nhận được mật khẩu mới."/>
    </c:if>
    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <!--====== End - Main Header ======-->

    <!--====== App Content ======-->
    <div class="app-content">

        <!-- <div class="u-s-p-b-60"> -->

        <!--====== Section Intro ======-->
        <div class="section__intro m-b-30 m-t-50">
            <div class="container">
                <h1 class="section__heading ">ĐĂNG NHẬP</h1>
            </div>
        </div>
        <!--====== End - Section Intro ======-->


        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
                <div class="row row--center">
                    <div class="col-lg-6 m-b-30">
                        <div class="l-f-o">
                            <div class="l-f-o__pad-box">
                                <c:if test="${msg != null}">
                                    <div class="alert alert-${alert} alert-dismissible fade show" role="alert">
                                            ${msg}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <h1 class="gl-h1">BẠN CHƯA CÓ TÀI KHOẢN ?</h1>
                                <div class="m-b-15">
                                    <a class="l-f-o__create-link btn--e-transparent-brand-b-2"
                                       href="<c:url value="/signup"/>">Đăng ký tài khoản</a>
                                </div>
                                <h1 class="gl-h1">ĐĂNG NHẬP</h1>

                                <span class="gl-text m-b-30">Nếu bạn đã có tài khoản, vui lòng đăng nhập.</span>
                                <form id="signin-form" class="l-f-o__form" method="post" action="<c:url value="/login"/>">
                                    <div class="gl-s-api">
                                        <div class="m-b-15">
                                            <a class="gl-s-api__btn gl-s-api__btn--fb"><i class="fab fa-facebook-f"></i>
                                                <span>Đăng nhập với Facebook</span>
                                            </a>
                                        </div>
                                        <div class="m-b-15">

                                            <a class="gl-s-api__btn gl-s-api__btn--gplus" ><i
                                                    class="fab fa-google"></i>

                                                <span>Đăng nhập với Google</span></a>
                                        </div>
                                    </div>
                                    <div class="m-b-30 m-t-30">

                                        <label class="gl-label">TÊN ĐĂNG NHẬP <span
                                                class="required-check">*</span></label>

                                        <input class="input-text input-text--primary-style" type="text"
                                               name="username" data-rule="required" value="${username}" placeholder="Nhập tên đăng nhập"
                                               >
                                    </div>
                                    <div class="m-b-30">

                                        <label class="gl-label">MẬT KHẨU <span class="required-check">*</span></label>

                                        <input class="input-text input-text--primary-style" type="password"
                                               name="password" data-rule="required" value="${password}" placeholder="Nhập mật khẩu" >
                                    </div>
                                    <div class="gl-inline">
                                        <div class="m-b-30">
                                            <button class="btn btn--e-transparent-brand-b-2"
                                                    type="submit">ĐĂNG NHẬP
                                            </button>
                                        </div>
                                        <div class="m-b-30">
                                            <a class="gl-link" href="<c:url value="/lost-password"/>">Quên mật khẩu?</a>
                                        </div>
                                    </div>
                                    <div class="m-b-30">

                                        <!--====== Check Box ======-->
                                        <div class="check-box">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="rememberMe"
                                                       id="flexCheckDefault"
                                                <c:if test="${rememberMe != null}"> checked</c:if> >
                                                <label class="form-check-label" for="flexCheckDefault">
                                                    Nhớ mật khẩu
                                                </label>
                                            </div>
                                        </div>
                                        <!--====== End - Check Box ======-->
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->
        <!-- </div> -->
        <!--====== End - Section 2 ======-->
    </div>
    <!--====== End - App Content ======-->

    <!--====== Main Footer ======-->
    <%@ include file="/customer/common/footer.jsp" %>
</div>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script>
    var formHandle = document.querySelector('#signin-form');
    // Go to validation
    new Validator(formHandle, function (err, res) {
        return res;
    });
</script>
</body>

</html>
