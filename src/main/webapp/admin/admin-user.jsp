<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-user.css"/>">
</head>

<body class="no-skin">
<!-- navbar main-->
<%@ include file="/admin/common/header.jsp" %>
<!-- end navbar main-->
<div class="fix">
    <!-- navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- end navbar left-->

    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5>Quản lý khách hàng</h5>
                    </li>

                </ul>
                <!-- /.breadcrumb -->

                <div class="nav-search" id="nav-search">
                    <form class="form-search" action="<c:url value="/admin/user"/>" method="get">
                            <span class="input-icon">
                            <button style="border: none; background: none;position: absolute" type="submit">
                                <i class="ace-icon fa fa-search nav-search-icon"></i>
                            </button>
                                <input type="text" placeholder="Search ..." name="keyWord" value="${keyWord}"
                                       class="nav-search-input" id="nav-search-input" autocomplete="off" />
                            </span>
                    </form>
                </div>
                <!-- /.nav-search -->
            </div>

            <div class="page-content p-t-20">

                <div class="col-xs-12 d-flex flex-column align-items-center ">

                    <!-- PAGE CONTENT BEGINS -->
                    <div class="w-100 px-2" style="<c:if test="${listCustomer.size() <= 13}">height: 575px;</c:if>">
                        <div class="col-xs-12 p-r-0">
                            <table id="simple-table" class="table table-bordered table-hover"
                                   style="width: 100%;">
                                <tbody>
                                <tr>
                                    <th class="text-center" style="width: 200px;padding-bottom: 20px;padding-top: 20px;">Họ Tên</th>
                                    <th class="text-center" style="width: 200px;padding-bottom: 20px;padding-top: 20px;">Tên đăng nhập</th>
                                    <th class="text-center" style="width: 150px;padding-bottom: 20px;padding-top: 20px;">SĐT</th>
                                    <th class="text-center" style="width: 250px;padding-bottom: 20px;padding-top: 20px;">Email</th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Địa chỉ</th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Kích hoạt</th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Khóa tài khoản</th>
                                </tr>
                                <c:forEach var="item" items="${requestScope.listCustomer}">
                                    <tr>
                                        <td>${item.fullName()}</td>
                                        <td>${item.username}</td>
                                        <td>${item.phoneNumber}</td>
                                        <td>${item.email}</td>
                                        <td>${item.address}</td>
                                        <td class="status-${item.id}" style="height: 43px"><span class="badge badge-sm
                                                <c:if test="${item.status.equals(SystemConstant.ACTIVATED)}">
                                                      bg-success
                                                </c:if>
                                                <c:if test="${item.status.equals(SystemConstant.NOT_AUTHENTICATION)}">
                                                      bg-warning
                                                </c:if>
                                                <c:if test="${item.status.equals(SystemConstant.LOCKED)}">
                                                      bg-danger
                                                </c:if>
                                            ">${item.status}</span></td>
                                        <td class="text-center">
                                            <div class="hidden-sm hidden-xs btn-group">
                                                <c:if test="${item.status.equals(SystemConstant.ACTIVATED)}">
                                                    <button data-input-id="${item.id}" class="btn-lock"
                                                            title="Lock"><i
                                                            class="fa-solid fa-user-lock"></i></button>
                                                </c:if>
                                                <c:if test="${item.status.equals(SystemConstant.LOCKED)}">
                                                    <button data-input-id="${item.id}" class="btn-lock"
                                                            title="UnLock"><i
                                                            class="fa-solid fa-lock-open"></i></button>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- paginatation -->
                    <form id="formPaging" action="<c:url value="/admin/user"/>" method="get">
                        <input type="hidden" value="" id="page" name="page"/>
                        <input type="hidden" value="${keyWord}" id="keyWord" name="keyWord"/>
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

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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

        $('.btn-lock').on('click', function () {
            var inputData = $(this).data('input-id');
            var iconLock = $(this).find("i");
            var status = $(".status-" + inputData);
            var message = '';
            console.log(inputData);
            // using Ajax to send data to server
            $.ajax({
                type: "POST",
                url: "<c:url value="/admin/user"/>",
                data: JSON.stringify(inputData),
                contentType: "application/json; charset=utf-8",
                    success: function (response) {
                    if (iconLock.hasClass("fa-user-lock")) {
                        iconLock.removeClass("fa-user-lock");
                        iconLock.addClass("fa-lock-open");
                        status.html(`<span class="badge badge-sm bg-danger">${SystemConstant.LOCKED}</span>`);
                        message = 'Đã khóa thành công';
                    } else {
                        iconLock.removeClass("fa-lock-open");
                        iconLock.addClass("fa-user-lock");
                        status.html(`<span class="badge badge-sm bg-success">${SystemConstant.ACTIVATED}</span>`);
                        message = 'Mở khóa thành công';
                    }
                    Swal.fire({
                        icon: "success",
                        title: message,
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
                },
                error: function (error) {
                    console.log('that bai')
                    Swal.fire({
                        icon: "warning",
                        title: "Thay đổi không thành công!",
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
        });
    });


</script>
</body>

</html>