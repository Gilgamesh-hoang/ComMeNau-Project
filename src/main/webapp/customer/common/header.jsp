<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    ul,
    li {
        padding: 0px;
        margin: 0px;
        text-decoration: none;
    }
    .product-link {
        padding: 10px;
        display: block;
        color: grey;
        font-size: 14px;
        cursor: pointer;
    }
    .product-link:hover {
        background-color: gainsboro;
    }
</style>
<header class="header--style-1 header--box-shadow">
    <!--====== Nav 1 ======-->
    <nav class="primary-nav primary-nav-wrapper--border">
        <div class="container">
            <div class="primary-nav">
                <a class="main-logo" href="<c:url value="/home"/>">
                    <img src="<c:url value="/customer/images/logo/logo-3.jpg"/>" alt=""></a>

                <!--====== Search Form ======-->
                <div class="main-form">
                    <input class="input-text input-text--border-radius input-text--style-1" type="text"
                           id="main-search" placeholder="Tìm kiếm món ăn">
                    <ul style="width: 100%;
                        position: absolute;
                        background: white;
                        z-index: 1000;"
                        id="searchResults">
                    </ul>

                </div>
                <!--====== End - Search Form ======-->

                <div class="" id="navigation">
                    <!--====== List ======-->
                    <ul class="search ah-list ah-list--design1 ah-list--link-color-secondary">
                        <li class="has-dropdown" data-tooltip="tooltip" data-placement="left">
                            <a><i class="fa-regular fa-circle-user"></i>
                                <c:if test="${auth!=null}">
                                    ${auth.firstName}
                                </c:if>
                            </a>
                            <ul class="sub-menu" style="width:120px">
                                <c:if test="${auth==null}">
                                    <li>
                                        <a href="<c:url value="/signup"/>"><i class="fa-solid fa-user-plus m-r-6"></i>
                                            <span>Đăng kí</span></a>
                                    </li>
                                    <li>
                                        <a href="<c:url value="/login"/>"><i class="fa-solid fa-lock m-r-6"></i>
                                            <span>Đăng nhập</span></a>
                                    </li>
                                </c:if>
                                <c:if test="${auth!=null}">
                                    <li>
                                        <a href="<c:url value="/profile"/>"><i class="fa-solid fa-circle-user m-r-6"></i>
                                            <span>Tài khoản</span></a>
                                    </li>
                                    <li>
                                        <a href="<c:url value="/logout"/>"><i
                                                class="fa-solid fa-lock-open m-r-6"></i>
                                            <span>Đăng xuất</span></a>
                                    </li>
                                </c:if>
                            </ul>
                        </li>
                        <li data-tooltip="tooltip" data-placement="left">
                            <a href="tel:+0900901904"><i class="fa-solid fa-phone-volume"></i></a>
                        </li>
                        <li data-tooltip="tooltip" data-placement="left">
                            <a href="mailto:contact@domain.com"><i class="far fa-envelope"></i></a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <!--====== Nav 2 ======-->
    <nav class="secondary-nav-wrapper">
        <div class="container">
            <div class="secondary-nav">
                <div style="width: 92px; height: 67px;"></div>
                <div class="" id="navigation2">
                    <!--====== Menu ======-->
                    <ul class="ah-list ah-list--design2 ah-list--link-color-secondary ">
                        <li>
                            <a href="<c:url value="/menu"/>">THỰC ĐƠN</a>
                        </li>
                        <li>
                            <a href="<c:url value="/blogs"/>">BÀI VIẾT</a>
                        </li>
                        <li>
                            <a href="<c:url value="/contacts"/>">LIÊN LẠC</a>
                        </li>
                    </ul>

                    <!--====== End - Menu ======-->
                </div>
                <div class="" id="navigation3">
                    <ul class="ah-list ah-list--design1 ah-list--link-color-secondary">
                        <li>
                            <a href="<c:url value="/home"/>"><i class="fa-solid fa-house"></i></a>
                        </li>
                        <li>
                            <a href="<c:url value="/wishlist"/>"><i class="far fa-heart"></i></a>
                        </li>
                        <li class="has-dropdown">
                            <a href="<c:url value="/carts"/>"><i class="fas fa-shopping-bag"></i></a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
    <!--====== End - Nav 2 ======-->
</header>
<script>
    $(document).ready(function () {
        var $searchInput = $("#main-search");
        var $searchResults = $("#searchResults");

        $searchInput.on("input", function () {
            // Clear previous results
            $searchResults.empty();
            // Get the input value
            var query = $(this).val().toLowerCase();
            if (query !== '') {
                $.ajax({
                    type: "GET",
                    url: "${pageContext.request.contextPath}/search?query="+ query,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {
                        var data = JSON.parse(response);
                        data.forEach(element => {
                            var link = "${pageContext.request.contextPath}/product/" + element.id;
                            $searchResults.append("<a href="+  link + " class=product-link>" + element.name + "</a>");
                            // $('#searchResults li:gt(7)').remove();
                        });
                    }
                });
            }
        });
        // Event listener for the input field
        $(document).on("mousedown", function (event) {
            // Kiểm tra xem phần tử được click có là con của #yourDiv hay không
            if ((!$searchResults.is(event.target) && ! $searchResults.has(event.target).length)
                    || ($searchInput.is(event.target) && !$searchInput.has(event.target).length)){
                $searchInput.val("");
                $searchResults.empty();
            }
        });


    });

</script>
