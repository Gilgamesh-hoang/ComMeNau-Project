<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Đăng ký</title>
  <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
  <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

  <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
  <link rel="stylesheet" href="<c:url value="/customer/css/signup.css"/>">

</head>
<body>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<!--====== End - Main Header ======-->
<!--====== App Content ======-->
<div class="app-content">
  <!--====== Section Intro ======-->

  <div class="section__intro m-b-60 m-t-50">
    <div class="container">
      <h1 class="section__heading ">ĐĂNG KÝ TÀI KHOẢN</h1>
    </div>
  </div>
  <!--====== End - Section Intro ======-->


  <!--====== Section Content ======-->
  <div class="section__content">
    <div class="container">
      <div class="row row--center">
        <div class="col-lg-6">
          <div class="l-f-o m-b-60">
            <div class="l-f-o__pad-box">
              <h1 class="gl-h1 l-s">THÔNG TIN CÁ NHÂN</h1>
              <form id="signup-form" class="l-f-o__form" action="<c:url value="/signup"/>" method="post">
                <div class="form-inline">
                  <div class="m-b-30 form-input m-r-8">
                    <label class="gl-label" >Họ <span class="required-check">*</span></label>
                    <input name="lastName" data-rule="required|containsAllWhitespace" value="${user.lastName}" class="input-text input-text--primary-style" type="text" placeholder="Nhập họ của bạn">
                  </div>
                  <div class="m-b-30 form-input">
                    <label class="gl-label">Tên <span class="required-check">*</span></label>
                    <input name="firstName" data-rule="required|containsAllWhitespace" value="${user.firstName}" class="input-text input-text--primary-style" type="text"  placeholder="Nhập tên của bạn">
                  </div>
                </div>
                <div class="m-b-30 form-input">
                  <label class="gl-label">Email <span class="required-check">*</span></label>
                  <input name="email" data-rule="required|email" value="${user.email}" class="input-text input-text--primary-style" type="email" placeholder="Nhập địa chỉ email">
                  <lable class="error-input">${emailError}</lable>
                </div>

                <div class="m-b-30 form-input">
                  <label class="gl-label">Số điện thoại <span class="required-check">*</span></label>
                  <input name="phoneNumber" data-rule="required|phone" value="${user.phoneNumber}" class="input-text input-text--primary-style" type="text" placeholder="Nhập số điện thoại">
                </div>
                <div class="m-b-30 form-input">
                  <label class="gl-label">Địa chỉ</label>
                  <input name="address" value="${user.address}" class="input-text input-text--primary-style" type="text" placeholder="Nhập địa chỉ">
                </div>
                <div class="m-b-30 form-input">
                  <label class="gl-label">Tên đăng nhập <span class="required-check">*</span></label>
                  <input name="username" data-rule="required|containsWhitespace" value="${user.username}" class="input-text input-text--primary-style" type="text" placeholder="Nhập tên đăng nhập">
                  <lable class="error-input">${usernameError}</lable>
                </div>
                <div class="m-b-30 form-input">
                  <label class="gl-label" >Mật khẩu <span class="required-check">*</span></label>
                  <input id="password" data-rule="required|minlength-6|containsWhitespace" name="password" class="input-text input-text--primary-style" type="password" placeholder="Nhập mật khẩu">
                </div>
                <div class="m-b-30 form-input">
                  <label class="gl-label" >Nhập lại mật khẩu <span class="required-check">*</span></label>
                  <input name="confirmPassword" data-rule="required|minlength-6|containsWhitespace|confirmed" class="input-text input-text--primary-style" type="password" placeholder="Nhập lại mật khẩu">
                </div>
                <div class="m-b-15">
                  <input class="btn btn-submit btn--e-transparent-brand-b-2" type="submit" value="ĐĂNG KÝ"/>
                </div>
                <a class="gl-link" href="<c:url value="/home"/>">Trở lại trang chủ</a>
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


<script src="<c:url value="/validate/validator.js"/>"></script>

<script>
        var formHandle = document.querySelector('#signup-form');
        var options = {
            // set a custom rule
            rules: {
                confirmed: function (value) {
                    var passwordValue = document.querySelector('input[name="password"]').value;
                    // Check if confirmPassword matches the password
                    return (value === passwordValue);
                }
            },
            messages: {
                vi: {
                    confirmed: {
                        empty: 'Vui lòng nhập trường này',
                        incorrect: 'Mật khẩu không khớp. Vui lòng nhập lại.'
                    }
                }
            }
        };
        // Go to validation
        new Validator(formHandle, function (err, res) {
            return res;
        }, options);
</script>
</body>
</html>