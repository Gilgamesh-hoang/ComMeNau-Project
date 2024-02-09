<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:url value="/admin/chat" var="adminChatURL"/>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js" integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css " />">
    <link rel="stylesheet" href=" <c:url value="/admin/css/chat.css" />">
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
</head>

<body class="no-skin">
<!-- Main Header-->
<%@ include file="/admin/common/header.jsp" %>
<div class="fix">
    <!-- navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5>Quản lý khách hàng</h5>
                    </li>
                </ul>
            </div>
            <div class="page-content p-t-20" style="height: 566px;">
                <div class="d-flex flex-column align-items-center w-100 ">
                    <!-- PAGE CONTENT BEGINS -->
                    <main class="d-flex w-100" style="height: 550px">
                        <div style="max-height: 550px;min-height:550px;overflow-y: auto" class="col-4 border-right">
                            <div class="px-4 d-none d-md-block">
                                <div class="d-flex align-items-center">
                                    <div class="flex-grow-1 " style="position: relative">
                                        <input id="main-search" type="text" class="form-control mt-3" placeholder="Search...">
                                        <ul style="width: 100%;
                        position: absolute;
                        background: white;
                        z-index: 1000;"
                                            id="searchResults">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div id="users" class="mt-3">
                            </div>
                            <hr class="d-block d-lg-none mt-1 mb-0">
                        </div>
                        <div class="chat-container col-8  border border-left" style="display: none ; min-height: 540px; max-height: 550px">
                            <div class="py-2 px-4 border-bottom d-none d-lg-block">
                                <div class="d-flex align-items-center py-1">
                                    <div class="position-relative">
                                        <img src="images/avatar-icon.jpg" class="rounded-circle mr-1" alt="1"
                                             width="40" height="40">
                                    </div>
                                    <div class="flex-grow-1 pl-3 ms-2">
                                        <strong id="username">None</strong>
                                    </div>

                                </div>
                            </div>

                            <div class="position-relative" style="min-height: 400px">
                                <div class="chat-messages p-4">

                                </div>
                            </div>

                            <div class="flex-grow-0 py-3 px-4 border-top">
                                <div class="d-flex align-items-center justify-content-center">
                                    <input type="text" id="chat-input" placeholder="Send a message..."/>
                                    <button class="chat-submit" id="chat-submit"><i
                                            class="material-icons">send</i></button>
                                </div>
                            </div>
                        </div>

                    </main>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/admin/common/footer.jsp" %>

</body>
<script>
    $(document).ready(function () {
        // Get adminId from session scope
        var adminId = ${sessionScope.auth.id};
        // Get userId from the URL
        var userId = window.location.search.substring(1).split("&")[0].split("=")[1];
        var section = 1;
        // Section for handling WebSocket connection
        const socket = new WebSocket("ws://localhost:8080/chat/" + adminId);

        socket.onopen = function (event) {};
        socket.onmessage = function (event) {
            // Handle incoming messages from WebSocket
            var chatMessage = JSON.parse(event.data);
            var type = (adminId == chatMessage.senderId) ? 'admin' : 'user';
            generateMessage(chatMessage.content, type, chatMessage.time, false);
            loadUsers(false , true);
        };
        socket.onclose = function (event) {};

        // Load users and messages based on userId
        if (userId != null) {
            loadUsers(false , true)
            updateView(userId)
        }
        else{
            loadUsers(true , true);
        }

        // Function to format time
        function formatTime(date, format) {
            var hours = date.getHours();
            var minutes = date.getMinutes();
            // Add leading zero if the hour or minute has only one digit
            hours = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;
            // Replace formatting elements
            format = format.replace("hh", hours);
            format = format.replace("mm", minutes);
            return format;
        }

        // Update message view for a specific user
        function updateView(userId) {
            var data = {
                participantId: userId,
                ownerId: adminId
            };
            $.ajax({
                type: "PUT",
                url: "<c:url value="/message"/>",
                data: JSON.stringify(data),
                contentType: "application/json; charset=UTF-8",
                success: function (response) {
                }
            });
        }

        // Load messages based on userId and section
        function loadMessages(userId) {
            var data = {
                participantId: userId,
                section: section
            };
            $.ajax({
                type: "GET",
                url: "<c:url value="/message"/>",
                data: $.param(data),
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (response) {
                    section++;
                    response.forEach(n => {
                        if (n.senderId == adminId)
                            generateMessage(n.content, 'admin', n.time, true);
                        else generateMessage(n.content, 'user', n.time, true);
                    })
                    return true;
                }
                , error: function (error) {
                    return false;
                }
            });
        }

        // Handle scroll event for chat messages
        $(".chat-messages").scroll(function () {
            if ($(this).scrollTop() === 0) {
                if (loadMessages(userId)) {
                    updateView();
                    var positionToScroll = $(".chat-messages").find(".chat-message").eq(0).position();
                    $(".chat-messages").scrollTop(positionToScroll.top);
                }
            }
        });

        //Load user list based on the condition
        function loadUsers(loadMessage , mark) {
            $.ajax({
                type: "GET",
                url: "<c:url value="/admin/chat/users"/>",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (response) {
                    $("#users").empty();
                    for (let i = response.length - 1; i >= 0; i--) {
                        var time = new Date(response[i].message.time);
                        var noti = !response[i].message.viewed && response[i].message.senderId !== 0;
                        var bold = '';
                        var badge = ``;
                        if (noti && mark) {
                            bold = "fw-bold";
                            badge = `<div class="badge bg-success ">!</div>`;
                        }
                        var content = response[i].message.content.length < 30 ? response[i].message.content :  response[i].message.content.toString().substr(0,30).concat("...");
                        var user = ` <a href="${adminChatURL}?user=` + response[i].userId + `" class="user-chat d-flex flex-row justify-content-between px-5 pb-3 border-0">
                            <div class="d-flex align-items-start">
                                <img src="images/avatar-icon.jpg" class="rounded-circle me-3" alt="2"width="40" height="40">
                                <div class="flex-grow-1 ml-3 ">`+ response[i].fullName +`<div class="` + bold + `">` + content + `</div></div>
                            </div>
                            <div class="float-right">` + badge + `
                                <div class="fw-bold">` + formatTime(time , "hh:mm") + `</div>
                            </div>
                        </a>`

                        $("#users").append(user);
                        if(userId == response[i].userId){
                            $(".chat-container").css("display","block")
                            $("#username").text(response[i].fullName)
                            updateView(userId);
                            loadMessages(userId);
                            $(".chat-messages").stop().animate({scrollTop: $(".chat-messages")[0].scrollHeight}, 1000);
                        }
                    }

                }
            });
        }

        // Handle click event for chat submit button
        $(".chat-submit").click(function (e) {
            e.preventDefault();
            var msg = $("#chat-input").val();
            if (msg.trim() === '') {
                return false;
            }
            generateMessage(msg, 'admin', new Date().getTime(), false);
            var message = {senderId:adminId, recipientId: userId, msg: msg};
            socket.send(JSON.stringify(message));
            $("#chat-input").val('');
            $(".chat-messages").stop().animate({scrollTop: $(".chat-messages")[0].scrollHeight}, 1000);
        });

        function generateMessage(msg, type, time, pre) {
            var sendTime = new Date(time);
            var imgSrc = type === 'user' ? "images/avatar-icon.jpg" : "images/admin-icon.jpg";
            var str = `<div class="chat-message chat-message-` + type + ` pb-4">
                <div>
                    <img src="` + imgSrc + `"
                         class="rounded-circle mr-1" alt="3" width="40" height="40">
                </div>
                <div class="message flex-shrink-1 bg-light rounded">
                   ` + msg + `
                </div>
                <div class="time">
                    ` +formatTime(sendTime,"hh:mm")+ `
                </div>
            </div>`
            if (pre) {
                $(".chat-messages").prepend(str);
            } else {
                $(".chat-messages").append(str);
            }
        }

        // Search functionality
        $(document).on("mousedown", function (event) {
            if ((!$('#searchResults').is(event.target) && ! $('#searchResults').has(event.target).length)
                || ($('#main-search').is(event.target) && !$('#main-search').has(event.target).length)){
                $('#main-search').val("");
                $('#searchResults').empty();
            }
        });
        $('#main-search').on("input",function (){
            $('#searchResults').empty();
            var keyword = $(this).val().toLowerCase();
            if (keyword !== '') {
                $.ajax({
                    type: "GET",
                    url: "<c:url value='/admin/chat/users'/>?keyword=" + encodeURIComponent(keyword),
                    success: function (response) {
                        response.forEach(element => {
                            $('#searchResults').append("<a href='/admin/chat?user="+  element.userId + "' class='product-link'>" + element.fullName + "</a>");
                            $('#searchResults li:gt(7)').remove();
                        });
                    }
                });
            }
        });
    });
</script>
</html>
