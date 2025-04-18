<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý thể loại</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-category.css"/>">
</head>
<body class="no-skin">
<!--====== Main Header ======-->
<%@ include file="/admin/common/header.jsp" %>
<!--====== End - Main Header ======-->

<div class="fix">
    <!-- navbar left-->

    <!-- end navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5>Quản lý thể loại</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <div class="table-btn-controls  d-flex flex-row-reverse text-right p-t-10">
                        <ul>
                            <li>
                                <button class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                        title='Thêm thể loại' id="addRow"> <span>
                                            <i class="fa-solid fa-circle-plus bigger-110 purple"></i>
                                        </span>
                                </button>
                            </li>
                            <li>
                                <button id="btnDelete" type="button"
                                        class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                        title='Xóa thể loại'>
                                        <span> <i class="fa-regular fa-trash-can bigger-110"></i>
                                        </span>
                                </button>
                            </li>
                        </ul>
                    </div>

                    <!-- PAGE CONTENT BEGINS -->
                    <div class="w-100 px-2" style="<c:if test="${categories.size() <= 10}">height: 575px;</c:if>">
                        <div class="col-xs-12 p-r-0 ">
                            <table id="simple-table" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center"
                                        style="width: 100px; padding-bottom: 20px;padding-top: 20px;">
                                        <label class="pos-rel">
                                            <input type="checkbox" class="form-check-input" id="checkAll"
                                                   style="border: 1px solid #ccc"/>
                                            <span class="lbl"></span>
                                        </label>
                                    </th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Mã thể
                                        loại
                                    </th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Tên thể
                                        loại
                                    </th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="item" items="${categories}">
                                    <tr>
                                        <td class="text-center">
                                            <label class="pos-rel">
                                                <input type="checkbox" class="form-check-input checkbox"
                                                       value="${item.id}"/>
                                                <span class="lbl"></span>
                                            </label>
                                        </td>
                                        <input type="hidden" name="id" id="category-id" value="${item.id}">

                                        <td class="category-col">
                                            <input type="text" name="code" value="${item.code}" disabled>
                                        </td>
                                        <td class="category-col">
                                            <input type="text" name="name" value="${item.name}" disabled>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button class="d-flex align-items-center justify-content-center btn btn-xs btn-info"
                                                        title='Sửa thể loại'>
                                                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                                                </button>
                                                <button type="submit"
                                                        class="d-flex align-items-center justify-content-center btn btn-xs btn-success"
                                                        title='Lưu thể loại'>
                                                    <i class="ace-icon fa fa-check bigger-120"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- paginatation -->
                    <form id="formPaging" action="<c:url value="/admin/categories"/>" method="get">
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

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>

<script src="<c:url value="/jquery/jquery.twbsPagination.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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


        // Bắt sự kiện khi nút "Edit" được nhấn
        $(document).on("click", ".btn-info", function (e) {
            e.preventDefault(); // Ngăn chặn việc submit form
            console.log('btn-info')
            // Tìm thẻ <input> bên cạnh nút "Edit" trong cùng một hàng
            var $inputElements = $(this).closest("tr").find("input[type='text']").not("#category-id");


            // Gỡ bỏ thuộc tính "disabled" trên các thẻ <input>
            $inputElements.prop("disabled", false);
        });
        // Bắt sự kiện khi nút "Submit" được nhấn
        $(document).on("click", ".btn-success", function () {
            // Tìm ra các phần tử input trong hàng được nhấn nút
            var $rowInputs = $(this).closest("tr").find("input[type='text'], input[type='hidden']");
            var dataToSend = {};

            $rowInputs.each(function () {
                dataToSend[$(this).attr("name")] = $(this).val();
            });
            // Gửi dữ liệu đến server bằng Ajax
            $.ajax({
                url: '<c:url value="/admin/categories"/>',
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(dataToSend),
                success: function (response) {
                    Swal.fire({
                        icon: "success",
                        title: "Thêm mới thành công",
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
                        window.location.href = "<c:url value="/admin/categories"/>";
                    }, 800);
                },
                error: function (error) {
                    console.log('that bai')
                    Swal.fire({
                        icon: "warning",
                        title: "Thêm mới thất bại!",
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
                }
            });
        });


        // Bắt sự kiện khi nút "Thêm thể loại" được nhấn
        $("#addRow").on("click", function () {
            var tableBody = $("#simple-table tbody");

            // Tạo một dòng mặc định
            var defaultRowHTML =
                `
                    <tr>
                        <td class="text-center"><label class="pos-rel"> <input type="checkbox" class="form-check-input checkbox"/>
                            <span class="lbl"></span>
                        </label></td>
                            <input type="hidden" name="id" id="category-id" value="">
                            <td class="category-col"><input name="code" type="text" value="" ></td>
                            <td class="category-col"><input name="name" type="text" value="" ></td>
                            <td>
                                <div class="btn-group">
                                    <button type="button" class="d-flex align-items-center justify-content-center btn btn-xs btn-info" title='Sửa thể loại'>
                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                    </button>
                                    <button type="button" class="d-flex align-items-center justify-content-center btn btn-xs btn-success" title='Lưu thể loại'>
                                        <i class="ace-icon fa fa-check bigger-120"></i>
                                    </button>
                                </div>
                            </td>
                    </tr>`;
            // Append the new row to the table
            tableBody.append(defaultRowHTML);
        });
        //check all
        $("#checkAll").change(function () {
            $(".checkbox").prop('checked', $(this).prop("checked"));
        });

        $("#btnDelete").click(function () {
            Swal.fire({
                title: "Xóa thể loại?",
                text: "Bạn có chắc muốn xóa thể loại này không!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var ids = [];
                    $('input[type="checkbox"]:checked').each(function () {
                        if ($(this).attr('id') !== 'checkAll') {
                            ids.push($(this).val()); // Thêm giá trị của checkbox được chọn vào mảng
                        }
                    });
                    console.log(ids)
                    $.ajax({
                        url: '<c:url value="/admin/categories"/>',
                        method: 'DELETE',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(ids),
                        success: function () {
                            Swal.fire({
                                icon: "success",
                                title: "Xóa thành công",
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
                                window.location.href = "<c:url value="/admin/categories"/>";
                            }, 800);
                        },
                        error: function () {
                            console.log('that bai')
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa thất bại!",
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
                        }
                    })
                }
            });
        });
    });
</script>
</body>
</html>