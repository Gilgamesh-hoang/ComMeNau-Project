<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-16">
    <title>Chi Tiết Sản Phẩm</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/paginationjs/2.1.5/pagination.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <!-- Toastr CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
    <!-- Toastr JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/product-detail.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/pagination.css"/>">


</head>
<body>
<!--====== Main Header ======-->
<jsp:include page="common/header.jsp"></jsp:include>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--====== Main Content =====-->
<div class="container-content mt-5">
    <c:set var="product" value="${ requestScope.product}"/>
    <fmt:setLocale value="vi_VN"/>

    <!--===== Section 1 =====-->
    <div class="container">
        <div class="d-flex flex-row">

            <div class="col-5 px-3">
                <div class="d-flex flex-column">
                    <div class="contain-image">

                        <img id="main-image" src="<c:url value="/images/products/${product.images.get(0)}"/> "
                             alt="Image 1">

                    </div>
                    <div class="slider" id="imageSlider">
                        <c:forEach var="image" items="${product.images}">
                            <img src="/images/products/${image}" alt="Image 1">
                        </c:forEach>
                        <!-- Add more images as needed -->
                    </div>
                </div>
            </div>
            <div class="col-7">

                <!--====== Product Right Side Details ======-->
                <div class="pd-detail">
                    <div>
                        <span class="pd-detail__name">${product.name}</span>
                    </div>
                    <div>
                        <div class="pd-detail__inline">

                            <span class="pd-detail__price"><fmt:formatNumber
                                    value="${RoundUtil.roundPrice((1 - product.discount) * product.price)}"
                                    type="currency"
                                    maxFractionDigits="0" currencyCode="VND"/> </span>

                            <span class="pd-detail__discount">(giảm giá ${DecimalFormat("#").format(product.discount * 100)}% )</span>
                            <del
                                    class="pd-detail__del"><fmt:formatNumber value="${product.price}" type="currency"
                                                                             maxFractionDigits="0" currencyCode="VND"/>
                            </del>
                        </div>
                    </div>
                    <div class="u-s-m-b-15">
                        <div class="pd-detail__rating gl-rating-style">
                            <div>
                                <c:forEach begin="1" end="${product.rate}">
                                    <i class="fas fa-star"></i>
                                </c:forEach>

                                <c:forEach begin="1" end="${5 - product.rate}">
                                    <i class="far fa-star"></i>
                                </c:forEach>

                            </div>

                            <span class="pd-detail__review u-s-m-l-4">
    <a data-click-scroll="#view-review">${product.amountOfReview} Đánh giá</a></span>
                        </div>
                    </div>

                    <div class="u-s-m-b-15">

                        <span class="pd-detail__preview-desc">${product.description}</span>
                    </div>
                    <div class="u-s-m-b-15">
                        <div class="pd-detail__inline">

    <span id="addwishlist" class="pd-detail__click-wrap"><i id="wishlisticon"
                                                            class=" fa-heart far u-s-m-r-6"></i>

    Thêm vào danh sách yêu thích</span>
                        </div>
                    </div>
                    <div class="u-s-m-b-15 ">
                            <input type="hidden" id="productId" value="${product.id}">
                            <div class="pd-detail-inline-2">
                                <div class="u-s-m-b-15 me-3">
                                    <!--====== Input Counter ======-->
                                    <div class="input-counter">
                                        <span id="counter__minus" class="input-counter__minus fas fa-minus"></span>
                                        <input id="input-counter-value"
                                               class="input-counter__text input-counter--text-primary-style"
                                               type="text" name="quantity" value="1" data-min="1" data-max="${product.available}">
                                        <span id="counter__plus" class="input-counter__plus fas fa-plus"></span>
                                    </div>
                                    <!--====== End - Input Counter ======-->
                                </div>
                                <div class="u-s-m-b-15">
                                    <c:if test="${product.available > 0}">
                                        <button class="btn btn--e-brand-b-2" id="btn-add-cart" type="button">Thêm vào giỏ
                                            hàng
                                        </button>
                                    </c:if>
                                    <c:if test="${product.available <= 0}">
                                        <button class="btn btn--e-brand-b-2" type="button" disabled>Hết hàng</button>
                                    </c:if>
                                </div>
                            </div>
    <div class="mt-2 pd-detail__preview-desc"><span>Còn lại : ${product.available} </span></div>
                    </div>
                    <div class="u-s-m-b-15">

                        <span class="pd-detail__label u-s-m-b-8">Chính sách của sản phẩm:</span>
                        <ul class="pd-detail__policy-list">
                            <li><i class="fas fa-check-circle u-s-m-r-8"></i>

                                <span>Bảo vệ người mua hàng.</span>
                            </li>
                            <li><i class="fas fa-check-circle u-s-m-r-8"></i>

                                <span>Hoàn 100% hóa đơn nếu khách hàng không nhận được hàng.</span>
                            </li>
                            <li><i class="fas fa-check-circle u-s-m-r-8"></i>

                                <span>Trả lại sản phẩm nếu như không giống với mô tả.</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container mt-5">
        <!--==== Sub-Section 1 ====-->
        <div class="u-s-m-b-30">
            <ul class="nav pd-tab__list">
                <li class="nav-item">
                    <a class="nav-link active show" id="view-review" data-toggle="tab" href="#pd-rev">Nhận Xét
                        <span>(${product.amountOfReview})</span></a>
                </li>
            </ul>
        </div>

        <div class="u-s-m-b-30">
            <div class="d-flex flex-column">
                <!--====== Section Review =====-->
                <div class="pd-tab__rev">
                    <div class="u-s-m-b-30">
                        <div class="pd-tab__rev-score">
                            <div class="u-s-m-b-8">
                                <h2>${product.amountOfReview} Đánh giá - ${product.rate} (Sao)</h2>
                            </div>
                            <div class="gl-rating-style-2 u-s-m-b-8">
                                <div>
                                    <c:forEach begin="1" end="${product.rate}">
                                        <i class="fas fa-star"></i>
                                    </c:forEach>

                                    <c:forEach begin="1" end="${5 - product.rate}">
                                        <i class="far fa-star"></i>
                                    </c:forEach>

                                </div>
                            </div>
                            <div class="u-s-m-b-8">
                                <h4>Chúng Tôi muốn những nhận xét từ phía bạn</h4>
                            </div>

                            <span class="gl-text">Làm ơn cho chúng tôi biết suy nghĩ của bạn về sản phẩm</span>
                        </div>
                    </div>
                    <div class="u-s-m-b-30">
                        <form>
                            <div class="rev-f1__group d-flex flex-row justify-content-between">
                                <div class="u-s-m-b-15">
                                    <h2>${product.amountOfReview} Đánh giá cho ${product.name}</h2>
                                </div>
                                <div class="u-s-m-b-15">

                                    <label for="sort-review"></label><select
                                        class="select-box select-box--primary-style" id="sort-review">
                                    <option class="rangeReview" value="desc">Sắp Xếp : Đánh giá tốt nhất</option>
                                    <option class="rangeReview" value="asc" selected>Sắp Xếp : Đánh giá tệ Nhất</option>
                                </select>
                                </div>
                            </div>
                            <div class="rev-f1__review">

                            </div>
                            <div id="pagination-container">
                            </div>
                        </form>
                    </div>

                </div>
                <!--====== Section Feed Back =====-->
                <c:if test="${not empty sessionScope.auth}">
                    <div>
                        <div class="u-s-m-b-30 pd-tab-feedback">
                            <form id="review" accept-charset="UTF-8">
                                <input type="hidden" name="productId" value="${product.id}">
                                <h2 class="u-s-m-b-15">Thêm một đánh giá</h2>
                                <span class="gl-text u-s-m-b-15">Tài khoản email của bạn sẽ không được công bố trong
                                    phần đánh giá. Trường yêu cầu được đánh dấu *</span>
                                <div class="u-s-m-b-30">
                                    <div class="rev-f2__table-wrap gl-scroll">
                                        <table class="rev-f2__table">
                                            <thead>
                                            <tr>
                                                <th>
                                                    <div class="gl-rating-style-2 d-flex ">
                                                        <div><i class="fas fa-star"></i></div>
                                                        <span>(1)</span>
                                                    </div>
                                                </th>

                                                <th>
                                                    <div class="gl-rating-style-2">
                                                        <div>
                                                            <i class="fas fa-star"></i><i class="fas fa-star"></i>
                                                        </div>
                                                        <span>(2)</span>
                                                    </div>
                                                </th>

                                                <th>
                                                    <div class="gl-rating-style-2">
                                                        <div><i class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i class="fas fa-star"></i>
                                                        </div>
                                                        <span>(3)</span>
                                                    </div>
                                                </th>

                                                <th>
                                                    <div class="gl-rating-style-2">
                                                        <div><i class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i class="fas fa-star"></i>
                                                        </div>
                                                        <span>(4)</span>
                                                    </div>
                                                </th>

                                                <th>
                                                    <div class="gl-rating-style-2">
                                                        <div><i class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i
                                                                class="fas fa-star"></i><i class="fas fa-star"></i>
                                                        </div>
                                                        <span>(5)</span>
                                                    </div>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>
                                                    <div class="radio-box">

                                                        <input type="radio" value="1" id="star-1" name="rating">
                                                        <div class="radio-box__state radio-box__state--primary">

                                                            <label class="radio-box__label" for="star-1"></label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="radio-box">

                                                        <input type="radio" value="2" id="star-2" name="rating">
                                                        <div class="radio-box__state radio-box__state--primary">

                                                            <label class="radio-box__label" for="star-2"></label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="radio-box">
                                                        <input type="radio" value="3" id="star-3" name="rating">
                                                        <div class="radio-box__state radio-box__state--primary">

                                                            <label class="radio-box__label" for="star-3"></label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="radio-box">
                                                        <input type="radio" value="4" id="star-4" name="rating">
                                                        <div class="radio-box__state radio-box__state--primary">

                                                            <label class="radio-box__label" for="star-4"></label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="radio-box">

                                                        <input type="radio" value="5" id="star-5" name="rating">
                                                        <div class="radio-box__state radio-box__state--primary">

                                                            <label class="radio-box__label" for="star-5"></label>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="rev-f2__group">
                                    <div class="u-s-m-b-15">

                                        <label class="gl-label" for="reviewer-text">Bình luận của bạn <span
                                                class="required-check">*</span></label>
                                        <textarea class="text-area text-area--primary-style" id="reviewer-text"
                                                  name="content" required></textarea>
                                    </div>

                                </div>
                                <div>

                                    <button class="btn btn--e-brand-shadow" type="submit">Bình luận</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <!--======= Relative Product Section ========-->
    <div class="section__content mb-5">
        <div class="section__intro u-s-m-b-46">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section__text-wrap">
                            <h1 class="section__heading u-c-secondary u-s-m-b-12">Các Món Ăn Được Đề Xuất</h1>

                            <span class="section__span text-silver">HẤP DẪN TỚI MIẾNG CUỐI CÙNG</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== Section NEW MENU ======-->
        <div class="list-product mt-5">
            <c:forEach var="item" items="${requestScope.relativeProducts}">

                <div class="container-product ">
                    <a href="<c:url value="/product/${item.id}"/>">
                        <div class="product-o product-o--hover-on">
                            <div class="product-o__wrap">
                                <div class="aspect aspect--bg-grey aspect--square u-d-block">
                                    <c:if test="${not empty item.avatar}">
                                        <img class="aspect__img" src="<c:url value="/images/products/${item.avatar}"/> "
                                             alt="No Image">
                                    </c:if>
                                </div>
                            </div>

                            <span class="product-o__category">
                                <a style="font-size: 14px;">${item.categoryName}</a>
                            </span>
                            <span class="product-o__name">
                                <a>${item.name}</a>
                            </span>
                            <div class="product-o__rating gl-rating-style">
                                <c:forEach begin="1" end="${item.rate}">
                                    <i class="fas fa-star"></i>
                                </c:forEach>

                                <c:forEach begin="1" end="${5 - item.rate}">
                                    <i class="far fa-star"></i>
                                </c:forEach>

                                <span class="product-o__review">(${item.amountOfReview})</span>
                            </div>

                            <c:set var="discount" value="${item.discount}"></c:set>
                            <span class="product-o__price"><fmt:formatNumber
                                    value="${RoundUtil.roundPrice(item.price * (1 - discount))}"
                                    type="currency" maxFractionDigits="0"
                                    currencyCode="VND"/>
                            <span class="product-o__discount"><fmt:formatNumber value="${item.price}"
                                                                                type="currency"
                                                                                maxFractionDigits="0"
                                                                                currencyCode="VND"/></span></span>
                        </div>
                    </a>
                </div>

            </c:forEach>
        </div>
    </div>
</div>
<!--====== Main Footer ======-->
<jsp:include page="common/footer.jsp"></jsp:include>
<script>


    $(document).ready(function () {
        var idProduct = ${requestScope.product.id};
        var reviewSize = ${requestScope.product.amountOfReview};
        $('#imageSlider').slick({
            slidesToShow: 4,
            slidesToScroll: 1,
            dots: false,
            centerMode: false,
            focusOnSelect: true,
            arrows: true,
            infinite: false,
            nextArrow: `<button type="button" class="slick-next">Next</button>`,
        });
        $('.contain-image').on('click', 'img', function () {
            // Lấy đường dẫn ảnh
            var imageUrl = $(this).attr('src');

            // Hiển thị ảnh lớn bằng FancyBox
            $.fancybox.open({
                src: imageUrl,
                type: 'image',
            });
        });
        $('.slider').on('click', 'img', function () {
            // Lấy đường dẫn ảnh
            var imageUrl = $(this).attr('src');
            $('#main-image').attr('src', imageUrl);

        });


        if (${not empty requestScope.inWishlists}) {
            $("#wishlisticon").addClass("fas");
            $("#wishlisticon").removeClass("far");
        } else {
            $("#wishlisticon").addClass("far");
            $("#wishlisticon").removeClass("fas");
        }
        $("#addwishlist").click(function () {
            var userId = ${requestScope.userId} +"";

            if ($("#wishlisticon").hasClass("fas")) {
                $.ajax({
                    type: "DELETE",
                    url: "<c:url value="/wishlist"/>",
                    data: JSON.stringify(idProduct),
                    contentType: "application/json;",
                    success: function (response) {
                        $("#wishlisticon").addClass("far");
                        $("#wishlisticon").removeClass("fas");
                        toastr.success("Xóa khỏi danh sách yêu thích thành công", '', { timeOut: 700 });
                    },
                    error: function (error) {
                        toastr.error("Xóa khỏi danh sách yêu thích thất bại", '', { timeOut: 700 });
                    }
                });

            } else {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/wishlist?productId=" + idProduct + "&userId=" + userId,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {
                        $("#wishlisticon").addClass("fas");
                        $("#wishlisticon").removeClass("far");
                        toastr.success("Thêm vào danh sách yêu thích thành công", '', { timeOut: 700 });
                    },
                    error: function (error) {
                        toastr.error("Thêm vào danh sách yêu thích Thất Bại", '', { timeOut: 700 });
                    }

                });

            }
        })


        // Dữ liệu mẫu
        const data = Array.from({length: reviewSize}, (_, index) => `Item ${index + 1}`);
        // Cấu hình và kích hoạt Pagination.js
        $('#pagination-container').pagination({
            dataSource: data,
            pageSize: 5,  // Số lượng mục trên mỗi trang
            callback: function (data, pagination) {
                // Hiển thị dữ liệu trên trang hiện tại
                displayData(pagination.pageNumber);
            },
        });


        $('#sort-review').change(function () {
            displayData(1)
        })

        function formatDateTime(date) {
            var day = date.getDate();
            var month = date.getMonth() + 1; // Tháng bắt đầu từ 0
            var year = date.getFullYear();
            var hours = date.getHours();
            var minutes = date.getMinutes();

            // Thêm số 0 đằng trước nếu giờ hoặc phút chỉ có một chữ số
            day = day < 10 ? "0" + day : day;
            month = month < 10 ? "0" + month : month;
            hours = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;

            // Định dạng: dd/mm/yyyy hh:mm
            var formattedDateTime = day + "/" + month + "/" + year + " " + hours + ":" + minutes;

            return formattedDateTime;
        }

        // Hàm hiển thị dữ liệu

        function displayData(pageNumber) {
            const container = $('.rev-f1__review');
            container.empty();
            $.ajax({
                url: "${pageContext.request.contextPath}/review?id=" + idProduct + "&page=" + pageNumber + "&sortBy=rating&sort=" + document.getElementById("sort-review").value,
                type: "GET",
                dataType: "json",
                success: function (response) {
                    response.forEach(n => {
                        var start = "";
                        for (let i = 0; i < n.rating; i++) {
                            start += `<i class="fas fa-star"></i>`
                        }
                        for (let i = 0; i < 5 - n.rating; i++) {
                            start += `<i class="far fa-star"></i>`
                        }
                        var date = formatDateTime(new Date(n.createdAt));

                        var append = `<div class="review-o u-s-m-b-15">
                                    <div class="review-o__info u-s-m-b-8">

                                        <span class="review-o__name">` + n.firstName + " " + n.lastName + `</span>
                                        <%-- Tạo một đối tượng Date từ timestamp --%>

                                        <span class="review-o__date">` + date + `</span>
                                    </div>
                                    <div class="review-o__rating gl-rating-style u-s-m-b-8">
                                        ` + start + `
                                        <span>(` + n.rating + `)</span>
                                    </div>
                                    <p class="review-o__text">` + n.content + `</p>
                                </div>`
                        container.append(append);
                    })

                },
                error: function (error) {

                }
            });

        }

        $("#review").submit(function (event) {
            event.preventDefault();

            // Sử dụng AJAX để gửi biểu mẫu
            $.ajax({
                type: "POST",
                url: "<c:url value="/review"/>",
                data: $("#review").serialize(), // Serialize dữ liệu biểu mẫu
                success: function (response) {
                    displayData(1)
                    $('#reviewer-text').val("");
                    $("input[name='rating']").prop("checked", false)
                    toastr.success("Bình luận thành công ", '', { timeOut: 700 });
                },
                error: function (error) {
                    toastr.error("Bình luận thất bại", '', { timeOut: 700 });
                }

            });
        });

        //validate quantity
        var min = parseInt($('#input-counter-value').attr('data-min'));
        var max = parseInt($('#input-counter-value').attr('data-max'));

        $('#input-counter-value').on('input', function () {
            var value = parseInt($(this).val());

            if (isNaN(value) || value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }

            $(this).val(value);
        });
        //Handle the click event of the increase button
        $('#counter__plus').on('click', function () {
            var inputElement = document.getElementById('input-counter-value');
            var newValue = parseInt(inputElement.value) + 1;
            if (newValue <= max )
                inputElement.value = newValue;
        });

        // Handle the click event of the decrease button
        $('#counter__minus').on('click', function () {
            var inputElement = document.getElementById('input-counter-value');
            var newValue = parseInt(inputElement.value) - 1;
            if (newValue >= min)
                inputElement.value = newValue;
        });

        $('#btn-add-cart').on('click', function () {
            var formData = {
                productId: $("#productId").val(),
                quantity: $("#input-counter-value").val()
            };
            console.log(formData);
            // Sử dụng AJAX để gửi biểu mẫu
            $.ajax({
                type: "POST",
                url: "<c:url value="/carts"/>",
                data: JSON.stringify(formData),
                contentType: "application/json; charset=utf-8",
                success: function () {
                    toastr.success('Đã thêm vào giỏ', '', { timeOut: 700 });
                },
                error: function () {
                    toastr.error('Không thêm được vào giỏ!', '', { timeOut: 700 });
                }
            });
        });
    });
</script>
</body>
</html>