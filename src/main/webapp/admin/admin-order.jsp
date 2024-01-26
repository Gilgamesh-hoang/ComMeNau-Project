<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page import="com.commenau.constant.SystemConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bài viết</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
</head>

<body class="no-skin">
<%@ include file="/admin/common/header.jsp" %>
<div class="fix">
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5>Quản lý đơn hàng</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content p-t-20">

                <div class="col-xs-12  d-flex flex-column align-items-center">

                    <!-- PAGE CONTENT BEGINS -->
                    <div class="w-100 px-2" style="height: 575px;">
                        <div class="col-xs-12 p-r-0 ">
                            <table id="simple-table" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center" style="width: 130px;padding-bottom: 20px;padding-top: 20px;">ID đơn hàng</th>
                                    <th class="text-center" style="width: 250px;padding-bottom: 20px;padding-top: 20px;">Tên khách hàng</th>
                                    <th class="text-center" style="width: 190px;padding-bottom: 20px;padding-top: 20px;">Giá trị đơn hàng</th>
                                    <th class="text-center" style="width: 140px;padding-bottom: 20px;padding-top: 20px;">Phí vận chuyển</th>
                                    <th class="text-center" style="width: 150px;padding-bottom: 20px;padding-top: 20px;">SĐT</th>
                                    <th class="text-center" style="width: 150px;padding-bottom: 20px;padding-top: 20px;">Trạng thái</th>
                                    <th class="text-center" style="width: 60px;padding-bottom: 20px;padding-top: 20px;"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${invoices}">
                                    <tr>
                                        <td class="text-center" >
                                            <p>${item.id}</p>
                                        </td>
                                        <td>${item.fullName}</td>
                                        <fmt:formatNumber value="${item.total}"
                                                          type="currency"
                                                          var="formattedTotal"/>
                                        <td class="text-center" >${formattedTotal}</td>
                                        <fmt:formatNumber value="${item.shippingFee}"
                                                          type="currency"
                                                          var="formattedFee"/>
                                        <td class="text-center" >${formattedFee}</td>
                                        <td class="text-center" >${item.phoneNumber}</td>
                                        <td class="text-center" ><span class="badge badge-sm
                                            <c:if test="${item.status==SystemConstant.INVOICE_DELIVERED}">bg-success</c:if>
                                            <c:if test="${item.status==SystemConstant.INVOICE_TRANSPORTING || item.status==SystemConstant.INVOICE_PROCESSING}">bg-warning</c:if>
                                            <c:if test="${item.status==SystemConstant.INVOICE_CANCEL}">bg-danger</c:if>
                                        ">${item.status}</span></td>
                                        <td class="text-center" >
                                            <div class="btn-group">
                                                <a href="<c:url value="/admin/invoiceDetail?id=${item.id}"/>"
                                                   class="d-flex align-items-center justify-content-center btn btn-xs btn-info">
                                                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- paginatation -->
<%--                    <nav aria-label="Page navigation example" style="text-align: center;">--%>
<%--                        <ul class="pagination">--%>
<%--                            <c:set var="currentPage" value="${nextPage}"/>--%>
<%--                            <c:set var="begin" value="${currentPage<=3 ? 1 : (maxPage <= 4 ? 1 : (currentPage>maxPage-3 ? maxPage-4 : currentPage-2))}"/>--%>
<%--                            <c:forEach var="index" begin="${begin}" end="${maxPage}"--%>
<%--                                       step="1">--%>
<%--                                <c:if test="${index < (begin + 5)}">--%>
<%--                                    <li class="page-item">--%>
<%--                                        <a class="page-link <c:if test="${nextPage == index}">active</c:if>"--%>
<%--                                           href="<c:url value="/admin/invoices?nextPage=${index}"/>">${index}</a>--%>
<%--                                    </li>--%>
<%--                                </c:if>--%>
<%--                            </c:forEach>--%>
<%--                        </ul>--%>
<%--                    </nav>--%>
                    <form id="formPaging" action="<c:url value="/admin/invoices"/>" method="get">
                        <input type="hidden" value="" id="page" name="page"/>
                    </form>
                    <nav aria-label="Page navigation">
                        <ul class="pagination" id="pagination"></ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/admin/common/footer.jsp" %>
<script src="<c:url value="/jquery/jquery.twbsPagination.js"/>"></script>

<script>
    $(document).ready(function () {
        //paging
        $(function () {
            var totalPages = ${maxPage};
            var currentPage = ${page};
            window.pagObj = $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: 10,
                startPage: currentPage,
                onPageClick: function (event, page) {
                    // console.info(page + ' (from options)');
                    if (currentPage !== page) {
                        $('#page').val(page);
                        $('#formPaging').submit();
                    }
                }
            });
        });
    });
</script>

</body>

</html>