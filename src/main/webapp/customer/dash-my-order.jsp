<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng của tôi</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-my-order.css"/>">
</head>

<body>
     <!--====== Main Header ======-->
     <%@ include file="/customer/common/header.jsp" %>
     <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->
    <!--========= Main-Content ===========-->
    <div class="container-content mt-5">

        <!--====== Section 1 ======-->
        <div class="u-s-p-b-60">

            <!--====== Section Content ======-->
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
                                <div
                                    class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white u-s-m-b-30">
                                    <div class="dash__pad-2">
                                        <h1 class="dash__h1 u-s-m-b-14">Đơn đặt hàng của tôi</h1>

                                        <span class="dash__text u-s-m-b-30">Tại đây bạn có thể nhìn thấy tất cả các đơn hàng trong quá trình xử lý</span>
                                        <form class="form_select m-order u-s-m-b-30" method="get" action="<c:url value="/invoices" />">
                                            <div class="m-order__select-wrapper">
                                                <label class="u-s-m-r-8">Hiển thị:</label>
                                                <select class="select-box select-box--primary-style" name="sortBy" id="my-order-sort">
                                                    <c:forEach var="entry" items="${sort}">
                                                        <option value="${entry.key}" <c:if test="${sortBy != null && entry.key eq sortBy}">selected</c:if>>${entry.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </form>
                                        <div class="dash__box dash__box--shadow dash__box--bg-white dash__box--radius">
                                            <h2 class="dash__h2 u-s-p-xy-20">Hóa đơn gần đây</h2>
                                            <div class="dash__table-wrap gl-scroll">
                                                <table class="dash__table">
                                                    <thead>
                                                        <tr>
                                                            <th>Mã hóa đơn</th>
                                                            <th>Thời gian</th>
                                                            <th>Trạng thái</th>
                                                            <th>Tổng giá trị</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="invoice" items="${requestScope.invoices}">
                                                            <tr>
                                                                <td>${invoice.id}</td>
                                                                <td>
                                                                    <fmt:formatDate value="${invoice.createdAt}" pattern=" YYYY-MM-dd hh:mma"/>
                                                                </td>
                                                                <td>
                                                                    <div class="dash__table-img-wrap">

                                                                        <span>${invoice.status}</span></div>
                                                                </td>
                                                                <td>
                                                                    <div class="dash__table-total">
                                                                        <fmt:formatNumber value="${invoice.total}"
                                                                                          type="currency"
                                                                                          var="formattedPrice"/>
                                                                        <span>${formattedPrice}</span>
                                                                        <div class="dash__link dash__link--brand">

                                                                            <a href="<c:url value="/invoice-details?id=${invoice.id}"/>">Chi tiết</a></div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
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
</body>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>

    // Sự kiện change cho thẻ select
    $(document).ready(function() {
        $('#my-order-sort').on('change', function() {
            $('.form_select').submit();
        });
    });
</script>
</html>