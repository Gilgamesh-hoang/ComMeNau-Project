<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/index.css"/>">

</head>
<body>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<fmt:setLocale value="vi_VN"/>
<content class="container-content">

    <!--========= Section Product Image Introduce  ==========-->
    <div id="product-introduce" class="carousel slide" data-bs-ride="carousel">

        <!-- Indicators/dots -->
        <div class="carousel-indicators" style="position: absolute !important;">
            <button type="button" data-bs-target="#product-introduce" data-bs-slide-to="0" class="active"></button>
            <button type="button" data-bs-target="#product-introduce" data-bs-slide-to="1"></button>
            <button type="button" data-bs-target="#product-introduce" data-bs-slide-to="2"></button>
        </div>

        <!-- The slideshow/carousel -->
        <div class="carousel-inner">
            <div class="carousel-item product-introduce-item active">
                <img src="<c:url value="/customer/images/slider/kimchi-fried-rice-241051_1280.jpg"/>" alt=""
                     class="d-block w-100">
            </div>
            <div class="carousel-item product-introduce-item">
                <img src="<c:url value="/customer/images/slider/sauerbraten-2512441_1280.jpg"/>" alt=""
                     class="d-block w-100">
            </div>
            <div class="carousel-item product-introduce-item">
                <img src="<c:url value="/customer/images/slider/menu-2509046_1280.jpg"/>" alt="" class="d-block w-100">
            </div>
        </div>

        <!-- Left and right controls/icons -->
        <button class="carousel-control-prev" type="button" data-bs-target="#product-introduce"
                data-bs-slide="prev">
            <span class="carousel-control-prev-icon"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#product-introduce"
                data-bs-slide="next">
            <span class="carousel-control-next-icon"></span>
        </button>
    </div>
    <div class="section__content">

        <!--====== Section Intro NEW MENU ======-->
        <div class="section__intro u-s-m-b-46">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section__text-wrap">
                            <h1 class="section__heading u-c-secondary u-s-m-b-12">MÓN ĂN MỚI</h1>

                            <span class="section__span text-silver">HẤP DẪN TỚI MIẾNG CUỐI CÙNG</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--====== Section NEW MENU ======-->
        <div class="section__content container-content">
            <div class="list-product">
                <c:forEach var="product" items="${requestScope.products}">
                    <div class="container-product ">
                        <div class="product-o product-o--hover-on">
                            <div class="product-o__wrap">
                                <a class="aspect aspect--bg-grey aspect--square u-d-block"
                                   href="<c:url value="/product/${product.id}"/>">

                                    <img class="aspect__img" src="<c:url value="/images/products/${product.avatar}"/> " alt=""></a>

                            </div>

                            <span class="product-o__category">

                                <a href="<c:url value='/menu?categoryId=1'/>">${product.categoryName}</a></span>

                            <span class="product-o__name">

                                <a href="<c:url value="/product/${product.id}"/>">${product.name}</a></span>
                            <div class="product-o__rating gl-rating-style"><c:forEach begin="1" end="${product.rate}">
                                <i class="fas fa-star"></i>
                            </c:forEach>

                                <c:forEach begin="1" end="${5 - product.rate}">
                                    <i class="far fa-star"></i>
                                </c:forEach>

                                <span class="product-o__review">(${product.amountOfReview})</span>
                            </div>

                            <c:set var="discount" value="${product.discount}"></c:set>
                            <span class="product-o__price"><fmt:formatNumber value="${RoundUtil.roundPrice(product.price * (1 - discount))}"
                                                                             type="currency" maxFractionDigits="0"
                                                                             currencyCode="VND"/>
                            <span class="product-o__discount"><fmt:formatNumber value="${product.price}"
                                                                                type="currency"
                                                                                maxFractionDigits="0"
                                                                                currencyCode="VND"/></span></span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!--=========  Feed-Back Section   ========-->
        <div class="u-s-p-b-90 u-s-m-b-30">

            <div class="section__intro u-s-m-b-46">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="section__text-wrap">
                                <h1 class="section__heading u-c-secondary u-s-m-b-12">PHẢN HỒI TỪ KHÁCH</h1>

                                <span class="section__span text-silver">WHAT OUR CLIENTS SAY</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!--====== Section Content ======-->
            <div class="section__content">
                <div class="container">

                    <!--====== Testimonial Slider ======-->
                    <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">

                        <div class="carousel-inner">
                            <div class="carousel-item active ">
                                <div class="d-flex flex-column align-items-center justify-content-center">
                                    <div class="testimonial__img-wrap">

                                        <img class="testimonial__img"
                                             src="<c:url value="/customer/images/promo/user-310807_1280.webp"/> "
                                             alt="">
                                    </div>
                                    <div
                                            class="testimonial__content-wrap d-flex flex-column align-items-center justify-content-center">

                                            <span class="testimonial__double-quote"><i
                                                    class="fas fa-quote-right"></i></span>
                                        <blockquote class="testimonial__block-quote">
                                            <p>" Món cơm cà ri thật sự ngon và thơm ngon. Hương vị cay từ gia vị cà ri
                                                kết hợp hoàn hảo với vị đậm đà từ thịt gà và sự ngon ngọt của cà tím và
                                                khoai tây."</p>
                                        </blockquote>

                                        <span class="testimonial__author">Nguyễn Văn A</span>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item  ">
                                <div class="d-flex flex-column align-items-center justify-content-center">
                                    <div class="testimonial__img-wrap">

                                        <img class="testimonial__img"
                                             src="<c:url value="/customer/images/promo/avatar-3637561_1280.png"/>"
                                             alt="">
                                    </div>
                                    <div
                                            class="testimonial__content-wrap d-flex flex-column align-items-center justify-content-center">

                                            <span class="testimonial__double-quote"><i
                                                    class="fas fa-quote-right"></i></span>
                                        <blockquote class="testimonial__block-quote">
                                            <p>" Món cơm cà ri thật sự ngon và thơm ngon. Hương vị cay từ gia vị cà ri
                                                kết hợp hoàn hảo với vị đậm đà từ thịt gà và sự ngon ngọt của cà tím và
                                                khoai tây."</p>
                                        </blockquote>

                                        <span class="testimonial__author">Nguyễn Văn A</span>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item  ">
                                <div class="d-flex flex-column align-items-center justify-content-center">
                                    <div class="testimonial__img-wrap">

                                        <img class="testimonial__img"
                                             src="<c:url value="/customer/images/promo/avatar-3637561_1280.png"/>"
                                             alt="">
                                    </div>
                                    <div
                                            class="testimonial__content-wrap d-flex flex-column align-items-center justify-content-center">

                                            <span class="testimonial__double-quote"><i
                                                    class="fas fa-quote-right"></i></span>
                                        <blockquote class="testimonial__block-quote">
                                            <p>" Món cơm cà ri thật sự ngon và thơm ngon. Hương vị cay từ gia vị cà ri
                                                kết hợp hoàn hảo với vị đậm đà từ thịt gà và sự ngon ngọt của cà tím và
                                                khoai tây."</p>
                                        </blockquote>

                                        <span class="testimonial__author">Nguyễn Văn A</span>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item  ">
                                <div class="d-flex flex-column align-items-center justify-content-center">
                                    <div class="testimonial__img-wrap">

                                        <img class="testimonial__img"
                                             src="<c:url value="/customer/images/promo/user-310807_1280.webp"/>" alt="">
                                    </div>
                                    <div
                                            class="testimonial__content-wrap d-flex flex-column align-items-center justify-content-center">

                                            <span class="testimonial__double-quote"><i
                                                    class="fas fa-quote-right"></i></span>
                                        <blockquote class="testimonial__block-quote">
                                            <p>" Món cơm cà ri thật sự ngon và thơm ngon. Hương vị cay từ gia vị cà ri
                                                kết hợp hoàn hảo với vị đậm đà từ thịt gà và sự ngon ngọt của cà tím và
                                                khoai tây."</p>
                                        </blockquote>

                                        <span class="testimonial__author">Nguyễn Văn A</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="carousel-indicators ">
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0"
                                    class="active" aria-current="true" aria-label="Slide 1"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"
                                    aria-label="Slide 2"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"
                                    aria-label="Slide 3"></button>
                            <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3"
                                    aria-label="Slide 4"></button>
                        </div>

                        <button class="carousel-control-prev" type="button"
                                data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button"
                                data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                    </div>
                    <div style="height: 50px;"></div>
                </div>
            </div>
        </div>
    </div>
</content>
<!--========= End - Main Content  ================-->
<%@ include file="/customer/common/footer.jsp" %>
</body>
</html>
