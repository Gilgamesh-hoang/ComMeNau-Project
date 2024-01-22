<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng của tôi</title>
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-my-order.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
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
                                <div class="dash__box dash__box--bg-white dash__box--shadow u-s-m-b-30">
                                    <div class="dash__pad-1">

                                        <span class="dash__text u-s-m-b-16">Xin Chào, ${auth.fullName()}</span>
                                        <ul class="dash__f-list">
                                            <li>
                                                <a href="<c:url value="/profile"/>">Thông tin tài
                                                    khoản</a>
                                            </li>

                                            </li>
                                            <li>
                                                <a class="dash-active" href="<c:url value="/invoices"/>">Đơn đặt
                                                    hàng</a>
                                            </li>

                                        </ul>
                                    </div>
                                </div>
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
                                                <label class="u-s-m-r-8" for="my-order-sort">Hiển thị:</label>
                                                <select
                                                    class="select-box select-box--primary-style" id="my-order-sort">
                                                    <option id="option10" <c:if test="${option.equals('option10')}">selected</c:if> >10 đơn đặt hàng cuối cùng</option>
                                                    <option id="optionAll" <c:if test="${option.equals('optionAll')}">selected</c:if> >Tất cả các đơn đặt hàng</option>
                                                </select>
                                            </div>
                                            <input type="hidden" value="" name="sortOption">
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
                                                        <c:forEach var="invoice" items="${requestScope.listInvoiceDTO}">
                                                            <tr>
                                                                <td>${invoice.id}</td>
                                                                <td>
                                                                    <fmt:formatDate value="${invoice.updatedAt}" pattern=" YYYY-MM-dd hh:mma"/>
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
            <!--====== End - Section Content ======-->
        </div>
        <!--====== End - Section 1 ======-->
    </div>
    <!--========= End-Main-Content ===========-->
     <!--====== Main Footer ======-->
     <%@include file="/customer/common/footer.jsp" %>
</body>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<%--<script src="<c:url value="/jquey/jquery.min.js"/>"></script>--%>
<script>
    $(document).ready(function() {
        $('#my-order-sort').on('change', function() {
            var selectedOption = $(this).find(':selected').attr('id');
            $('input[name="sortOption"]').val(selectedOption);
            $('.form_select').submit();
            <%--$.ajax({--%>
            <%--    type: "GET",--%>
            <%--    url: "<c:url value="/don-hang"/>",--%>
            <%--    data: { sortOption: selectedOption },--%>
            <%--    success: function(response) {--%>
            <%--        // location.reload();--%>
            <%--        $('.form_select').submit();--%>
            <%--        console.log("Success:", response);--%>
            <%--    },--%>
            <%--    error: function(error) {--%>
            <%--        console.error("Error:", error);--%>
            <%--    }--%>
            <%--});--%>
        });
    });
</script>
</html>