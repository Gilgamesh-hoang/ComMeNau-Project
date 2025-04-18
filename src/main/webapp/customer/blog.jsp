<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Bài viết</title>

    <!--====== font-awesome - bootstrap ======-->
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/blog.css"/> ">
</head>

<body>

<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->

    <!--====== App Content ======-->
    <div class="app-content">

        <!--====== Section 1 ======-->
        <div class="u-s-p-y-90">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-4 col-sm-12">
                        <div class="blog-w-master">
                            <div class="u-s-m-b-60">
                                <div class="blog-w">

                                    <span class="blog-w__h">TÌM KIẾM</span>
                                    <form class="blog-search-form" method="get" action="<c:url value="/blogs"/> ">
                                        <label for="post-search"></label>
                                        <input class="input-text input-text--primary-style" type="text"
                                               value="${keyWord}"
                                               id="post-search" name="keyWord" placeholder="Search" required>
                                        <button class="btn btn--icon" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </form>
                                </div>
                            </div>
                            <div class="u-s-m-b-60">
                                <div class="blog-w">

                                    <span class="blog-w__h">BÀI ĐĂNG GẦN ĐÂY</span>
                                    <ul class="blog-w__b-l">
                                        <c:forEach var="blog" items="${requestScope.newestBlogs}">
                                            <li>
                                                <div class="b-l__block">
                                                    <div class="b-1__head">
                                                        <img src="<c:url value="/images/blogs/${blog.image}"/>" alt="">
                                                        <div class="b-1__right">
                                                            <div class="b-l__date">
                                                                    <%-- Dung tablib fmt de format Date--%>
                                                                <fmt:formatDate value="${blog.createdAt}"
                                                                                pattern="dd 'Tháng' MM yyyy"
                                                                                var="formattedDate"/>
                                                                <span>${formattedDate}</span>
                                                            </div>
                                                            <span class="b-l__h">

                                                                <a href="<c:url value="/blog-detail?id=${blog.id}"/> ">${blog.title}</a>
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <span class="b-l__p">${blog.shortDescription}
                                                    </span>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-8 col-sm-12">
                        <c:forEach var="blogs" items="${requestScope.blogs}">
                            <div class="bp bp--img u-s-m-b-30">
                                <div class="bp__wrap">
                                    <div class="bp__thumbnail">

                                        <!--====== Image Code ======-->

                                        <a class="aspect aspect--bg-grey aspect--1366-768 u-d-block"
                                           href="<c:url value="/blog-detail?id=${blogs.id}"/>">

                                            <img class="aspect__img" src="<c:url value="/images/blogs/${blogs.image}"/>"
                                                 alt=""></a>
                                        <!--====== End - Image Code ======-->
                                    </div>
                                    <div class="bp__info-wrap">
                                        <div class="bp__stat">

                                            <span class="bp__stat-wrap">

                                                <span class="bp__publish-date">

                                                        <fmt:formatDate value="${blogs.createdAt}"
                                                                        pattern="dd 'Tháng' MM yyyy"/>
                                                        </span></span>

                                            <span class="bp__stat-wrap">
                                                <span class="bp__comment">
                                                    <i class="far fa-comments u-s-m-r-4"></i>
                                                        <span>${blogs.numReviews}</span></span></span>

                                        </div>
                                        <span class="bp__h1">${blogs.title}</span>
                                        <p class="bp__p">${blogs.shortDescription}</p>
                                        <div class="gl-l-r">
                                            <div>

                                                <span class="bp__read-more">
                                                    <a href="<c:url value="/blog-detail?id=${blogs.id}"></c:url>">ĐỌC THÊM</a></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <nav class="post-center-wrap u-s-p-y-60">
                            <!--====== Pagination ======-->
                            <ul class="blog-pg">
                                <c:forEach var="index" begin = "1" end="${maxPage}">
                                    <li class="<c:if test="${index==page}">blog-pg--active</c:if>">
                                        <a href="<c:url value="/blogs?page=${index}&keyWord=${keyWord}"/>">${index}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>
</div>

</body>

</html>