<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Trang chủ</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-home.css"/>">
</head>
<body class="no-skin">
<!--====== Main Header ======-->
<%@ include file="/admin/common/header.jsp" %>
<!--====== End - Main Header ======-->

<div class="fix">
    <!-- navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h6>Trang chủ</h6>
                    </li>
                </ul>
                <!-- /.breadcrumb -->

            </div>

            <div class="page-content" style=" height: 663px;">
                <div class="col-xs-12  d-flex flex-column">
                    <ul class="product-list clearfix">
                        <li>
                            <p class="icon"><i class="fa-solid fa-user"></i></p>
                            <h3>Người dùng</h3>
                            <p class="text">${totalUser} người dùng.</p>
                        </li>
                        <li>
                            <p class="icon"><i class="fa-solid fa-chart-simple"></i></p>
                            <h3>Doanh thu trong ngày</h3>
                            <p class="text"><fmt:formatNumber value="${revenueOfDay}" type="currency" pattern="###,### VNĐ"/></p>
                        </li>
                        <li>
                            <p class="icon"><i class="fas fa-chart-line"></i></p>
                            <h3>Doanh thu trong tháng</h3>
                            <p class="text"><fmt:formatNumber value="${revenueOfMonth}" type="currency" pattern="###,### VNĐ"/></p>
                        </li>
                        <li>
                            <p class="icon"><i class="fa-solid fa-cart-plus"></i></p>
                            <h3>Bán được trong tháng</h3>
                            <p class="text">${sellingOfMonth} đơn hàng</p>
                        </li>
                        <li>
                            <p class="icon"><i class="fa-solid fa-xmark"></i></p>
                            <h3>Bán được trong ngày</h3>
                            <p class="text">${sellingOfDay} đơn hàng</p>
                        </li>
                        <li>
                            <p class="icon"><i class="fa-solid fa-medal"></i></p>
                            <h3>Sản phẩm bán chạy nhất</h3>
                            <c:forEach items="${bestSale}" var="item">
                                <p class="text">${item.key} (Số lượng: ${item.value})</p>
                            </c:forEach>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
</body>
</html>