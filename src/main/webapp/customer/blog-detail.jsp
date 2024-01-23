<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>--%>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/blog-detail.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/input.css"/>">
    <title>Chi tiết bài viết</title>
</head>

<body>
<!--====== Main Header ======-->
<%@include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--====== End - Main Header ======-->
<div class="container-content">

    <!--====== Section 1 ======-->
    <div class="u-s-p-y-90">

        <!--====== Detail Post ======-->
        <div class="detail-post">
            <div class="bp-detail">
                <div>
                    <!--====== Image Code ======-->
                    <img class="bp-detail-image" src="<c:url value="/images/blogs/${requestScope.blog.image}"/>" alt="">
                    <!--====== End - Image Code ======-->
                </div>
                <div class="bp-detail__info-wrap">
                    <div class="bp-detail__stat">
                            <span class="bp-detail__stat-wrap">

                                <span class="bp-detail__publish-date">

                                    <a>
                                        <fmt:formatDate value="${requestScope.blog.createdAt}"
                                                        pattern="dd 'Tháng' MM yyyy"
                                                        var="formattedDate"/>
                                                            <span>${formattedDate}</span>
                                </span>
                            </span>

                        <span class="bp-detail__stat-wrap">

                                <span class="bp-detail__author">
                                    <a class="text-silver" href="#">${requestScope.blog.createdBy}</a>
                                </span>
                            </span>

                    </div>

                    <span class="bp-detail__h1">

                            <a>${blog.title}</a></span>

                    <div class="bp-detail__p"> ${blog.content} </div>

                </div>
            </div>
        </div>
    </div>
    <!--====== End - Detail Post ======-->
    <div class="u-s-p-b-60">

        <div class="d-meta__comment-arena" style="max-width: 90%;
    padding: 0 15px;
    margin: 0 auto">

            <span class="d-meta__text u-s-m-b-36">${numberReviews} bình luận trong bài viết này</span>
            <div class="d-meta__comments u-s-m-b-30">
                <ol>
                    <li>
                        <!--====== Comment ======-->
                        <c:forEach var="blr" items="${requestScope.listBlogReview}">
                            <div class="d-meta__p-comment">
                                <div class="p-comment__wrap1">
                                    <div class="aspect aspect--square p-comment__img-wrap">
                                        <img src="<c:url value="/customer/images/logo/user.png"/>" style="width: 30px"
                                             alt="">
                                    </div>
                                </div>
                                <div class="p-comment__wrap2">
                                    <span class="p-comment__author"> ${blr.fullName} </span>
                                    <span class="p-comment__timestamp">
                                                <fmt:formatDate value="${blr.createdAt}"
                                                                pattern="dd-MM-yyyy 'lúc' hh:mma"
                                                                var="formattedDate1"/>
                                            <span>${formattedDate1}</span>
                                    </span>
                                    <p class="p-comment__paragraph">${blr.content}</p>
                                </div>
                            </div>
                        </c:forEach>
                        <!--====== End - Comment ======-->

                    </li>
                </ol>
            </div>

            <c:if test="${auth != null}">
                <span class="d-meta__text-2 u-s-m-b-6">Tham gia vào cuộc trò chuyện</span>

                <span class="d-meta__text-3 u-s-m-b-16">Tài khoản email của bạn sẽ không được được công bố trong bình
                    luận.
                    Trường yêu cầu sẽ đánh dấu *</span>
                <form class="respond__form" method="post" action="<c:url value="/blog-detail"/>">
                    <div class="respond__group d-flex flex-row">
                        <div class="u-s-m-b-15 col-6 me-3">
                            <label class="gl-label" for="comment">Bình Luận *</label><textarea
                                class="text-area text-area--primary-style" name="content" id="comment"></textarea>
                        </div>
                    </div>
                    <div>
                        <button class="btn btn--e-brand-shadow" type="submit">Đăng Bình Luận</button>
                    </div>
                    <input type="hidden" name="blogId" value="${blog.id}">
                </form>
            </c:if>
        </div>

    </div>
    <!--====== End - Section 1 ======-->
</div>
<%@include file="/customer/common/footer.jsp" %>
</body>

</html>