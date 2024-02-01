<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Thực Đơn</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css"/>
    <script src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/paginationjs/2.1.5/pagination.js"></script>

    <!-- Toastr CSS -->
    <link rel="stylesheet" type="text/css"
    href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">

    <!-- Toastr JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/filter.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/pagination.css"/>">
</head>
<body class="config">

<!--====== Main App ======-->
<div id="app">
    <c:set var="product" value="${ requestScope.product}"/>
    <fmt:setLocale value="vi_VN"/>
    <!--====== Main Header ======-->
    <jsp:include page="common/header.jsp"></jsp:include>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->


    <!--====== App Content ======-->
    <div class="app-content">

        <!--====== Section 1 ======-->
        <div class="u-s-p-y-90">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-12">
                        <div class="shop-w-master">
                            <h1 class="shop-w-master__heading u-s-m-b-30"><i class="fas fa-filter u-s-m-r-8"></i>

                                <span>LỌC</span>
                            </h1>
                            <div class="shop-w-master__sidebar sidebar--bg-snow">
                                <div class="u-s-m-b-30">
                                    <div class="shop-w">
                                        <div class="shop-w__intro-wrap">
                                            <h1 class="shop-w__h">PHÂN LOẠI</h1>

                                        </div>
                                        <div class="shop-w__wrap collapse show" id="s-category">
                                            <ul class="shop-w__category-list gl-scroll">
                                                <li class="">
                                                    <a class="has-list" id="0" href="<c:url value='/menu'/>">Tất cả</a>
                                                </li>
                                                <c:forEach var="category" items="${requestScope.categories}">
                                                <li class="">
                                                    <a class="has-list" id="${category.id}" href="<c:url value='/menu?categoryId=${category.id}'/>">${category.name}</a>
                                                </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-12">
                        <div class="shop-p">
                            <div class="shop-p__toolbar u-s-m-b-30">
                                <div class="shop-p__tool-style">
                                    <div class="tool-style__group u-s-m-b-8">
                                    </div>
                                    <form>
                                        <div class="tool-style__form-wrap">

                                            <div class="u-s-m-b-8">
                                                <select id="sortingOptions" class="select-box select-box--transparent-b-2">
                                                    <option sortName="createdAt" sortBy="desc" selected>Mới nhất</option>
                                                    <option sortName="rate" sortBy="desc">Đánh giá tốt nhất</option>
                                                    <option sortName="price" sortBy="asc">Giá thấp nhất</option>
                                                    <option sortName="price" sortBy="desc">Giá cao nhất</option>
                                                </select>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="shop-p__collection">
                                <div class="products row is-grid-active">


                                </div>
                            </div>
                            <div class="u-s-p-y-60">
                                <!--====== Pagination ======-->
                                <div id="pagination-container">

                                </div>
                                <!--====== End - Pagination ======-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--====== Main Footer ======-->
    <jsp:include page="common/footer.jsp"></jsp:include>

</div>
<script>
    var categoryId = window.location.search.substring(1).split("=")[1];
    const buttons = document.querySelectorAll('.has-list');
    var reviewSize = ${requestScope.total};
    var sortName = undefined;
    var sortBy = undefined;
    if (!categoryId) {
        categoryId = 0;
    }
    markLink(categoryId)
    loadProducts(1);

    function markLink(categoryId) {
        // Remove class "selected" from all tags
        $(".shop-w__category-list a").removeClass("selected");

        // Add class "selected" to the clicked tag
        $("#" + categoryId).addClass("selected");
    }

    function loadProducts(pagination) {
        var url = "";
        if (sortName != undefined) {
            url = "${pageContext.request.contextPath}/menu/filter?id=" + categoryId + "&page=" + pagination + "&sortName=" + sortName + "&sortBy=" + sortBy
        } else {
            url = "${pageContext.request.contextPath}/menu/filter?id=" + categoryId + "&page=" + pagination;
        }
        $.ajax({
            type: "GET",
            url: url,
            success: function (response) {
                var re = JSON.parse(response);
                $(".products").empty();
                re.forEach(n => {
                    var star = ``;
                    for (let i = 0; i < n.rate; i++) {
                        star += ` <i class="fas fa-star"></i>`
                    }
                    for (let i = 0; i < 5 - n.rate; i++) {
                        star += ` <i class="far fa-star"></i>`
                    }
                    var discount = formatCurrency(n.price * (1 - n.discount));
                    var price = formatCurrency(n.price);

                    var wishlist;
                    if (n.wishlist) {
                        wishlist = "fas";
                    } else {
                        wishlist = "far";
                    }
                    var buttonContent = `<button class="btn--e-brand-b-2 btn-add-cart" data-input-id="` + n.id + `">THÊM VÀO GIỎ HÀNG</button>`;
                    if (n.available <= 0) {
                        buttonContent = `<button class="btn btn--e-brand-b-2" disabled>HẾT HÀNG</button>`;
                    }
                    var data = `<div class="col-lg-4 col-md-6 col-sm-6">
                                            <div class="product-m">
                                                <div class="product-m__thumb">

                                                    <a class="aspect aspect--bg-grey aspect--square u-d-block"
                                                       href="/product/` + n.id + `">

                                                        <img class="aspect__img" src="images/products/` + n.avatar + `"
                                                             alt=""></a>

                                                    <div class="product-m__add-cart">` + buttonContent + `
                                                    </div>
                                                </div>
                                                <div class="product-m__content">
                                                    <div class="product-m__category">

                                                        <a href="shop-side-version-2.html">` + n.categoryName + `</a>
                                                    </div>
                                                    <div class="product-m__name">

                                                        <a href="product-detail.html">` + n.name + `</a>
                                                    </div>
                                                    <div class="product-m__rating gl-rating-style">
                                                       ` + star + `

                                                        <span class="product-m__review">(` + n.amountOfReview + `)</span>
                                                    </div>
                                                    <span class="product-o__price">`+ discount +`
                                                        <span class="product-o__discount">`+ price +`</span>
                                                     </span>
                                                    <div class="product-m__hover">
                                                        <div class="product-m__preview-description">

                                                            <span>` + n.description + `</span>
                                                        </div>
                                                        <div class="product-m__wishlist">
                                                            <i  productId=` + n.id + ` class="wishlist ` + wishlist + ` fa-heart" href="#" data-tooltip="tooltip"
                                                               data-placement="top" title="Add to Wishlist"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`
                    $(".products").append(data);
                })


                var userId = ${sessionScope.auth.id}+"";
                $('.wishlist').click(function () {
                    var edit = $(this);
                    var method = "";
                    if (edit.hasClass("far")) {
                        method = "POST"
                    } else {
                        method = "DELETE"
                    }

                    $.ajax({
                        type: method,
                        url: "${pageContext.request.contextPath}/wishlist?productId=" + edit.attr('productId') + "&userId=" + userId,
                        success: function (response) {
                            if (edit.hasClass("far")) {
                                edit.removeClass("far")
                                edit.addClass("fas")
                            } else {
                                edit.removeClass("fas")
                                edit.addClass("far")
                            }
                        },
                        error: function (error) {
                        }

                    });
                });
            },
            error: function (error) {

            }

        });
    }



    function roundPrice(price) {
        return Math.round(price / 1000) * 1000;
    }

    function formatCurrency(amount) {
        const roundedAmount = roundPrice(amount);

        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        });

        return formatter.format(roundedAmount);
    }

    $(document).ready(function () {
        const data = Array.from({length: reviewSize}, (_, index) => `Item ${index + 1}`);
        //Pagination.js
        $('#pagination-container').pagination({
            dataSource: data,
            pageSize: 9,  // Số lượng mục trên mỗi trang
            callback: function (data, pagination) {
                loadProducts(pagination.pageNumber);
            },
        });

        //select a category
        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                $(".shop-w__category-list li a").removeClass("selected");
                $(this).addClass("selected");
            });
        });

        //sorting
        $('#sortingOptions').on('change', function() {
            sortName = $(this).children(":selected").attr("sortName")
            sortBy = $(this).children(":selected").attr("sortBy")
            loadProducts(1);
        });


    });

    //add a product to cart
    $(document).on('click','.btn-add-cart', function () {
        var inputData = {};
        inputData['productId'] = $(this).data('input-id');
        console.log(inputData);
        // using Ajax to send data to server
        $.ajax({
            type: "POST",
            url: "<c:url value="/carts"/>",
            data: JSON.stringify(inputData),
            success: function () {
                toastr.success("Đã thêm vào giỏ hàng", '', { timeOut: 700 });
            },
            error: function (error) {
                console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);
                toastr.error("Không thêm được vào giỏ", '', { timeOut: 700 });
            }
        });
    });


</script>
</body>
</html>
