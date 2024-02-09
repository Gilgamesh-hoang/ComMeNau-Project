<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="body">

    <div id="chat-circle" class="btn btn-raised">
        <div id="chat-overlay"></div>
        <i style="font-size: 40px;" class="material-icons">message</i>
        <span id="noti"></span>
    </div>

    <div class="chat-box">
        <div class="chat-box-header">
            ChatBot
            <span class="chat-box-toggle"><i class="material-icons">close</i></span>
        </div>
        <div class="chat-box-body">
            <div class="chat-box-overlay">
            </div>
            <div class="chat-logs">

            </div><!--chat-log -->
        </div>
        <div class="chat-input">
            <form class="d-flex align-items-center justify-content-center">
                <input type="text" id="chat-input" placeholder="Send a message..."/>
                <button type="submit" class="chat-submit" id="chat-submit"><i
                        class="material-icons">send</i></button>
            </form>
        </div>
    </div>

</div>
<script>
    $(document).ready(function () {
        // Get user ID from the session
        var userId = ${sessionScope.auth.id};

        // WebSocket connection to the chat server
        const socket = new WebSocket("ws://localhost:8080/chat/" + userId);

        // Section counter for loading messages
        var section = 1;

        // Array to store messages
        let messages = [];

        // WebSocket event handlers
        socket.onopen = function (event) {
        };

        socket.onmessage = function (event) {
            // Code to handle incoming WebSocket messages
            var message = JSON.parse(event.data);
            // Check if chat box is visible
            var viewed = false;
            if ($('.chat-box').css('display') === 'block') {
                viewed = true;
            }
            else{
                // Show notification badge if chat box is not visible
                if (!$('#noti').hasClass("notification-badge")) {
                    $('#noti').addClass("notification-badge");
                    $('#noti').text("!")
                }
            }
            // Generate message based on sender ID and type
            if (message.senderId == userId) {
                generateMessage(message.senderId, message.content, 'user', message.time, viewed, false);
            } else {
                generateMessage(message.senderId, message.content, 'admin', message.time, viewed, false);
            }
            // Update the viewed status
            updateViewed();

        };
        socket.onclose = function (event) {
        };

        // Function to update the viewed status
        function updateViewed() {
            if ($('.chat-logs').css('display') === 'block') {
                var data = {
                    participantId: userId,
                    ownerId: userId
                };
                // Send AJAX request to update viewed status on the server
                $.ajax({
                    type: "PUT",
                    url: "<c:url value="/message"/>",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=UTF-8",
                    success: function (response) {
                    }
                });
            }
        }

        // Function to load messages
        function loadMessages() {
            var data = {
                participantId: userId,
                section: section
            };
            // Send AJAX request to retrieve messages from the server
            $.ajax({
                type: "GET",
                url: "<c:url value="/message"/>",
                data: $.param(data),
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (response) {
                    if (response !== undefined) {
                        section++;
                        response.forEach(message => {
                            if (message.senderId == userId) {
                                generateMessage(message.senderId, message.content, 'user', message.time, message.viewed, true);
                            } else {
                                generateMessage(message.senderId, message.content, 'admin', message.time, message.viewed, true);
                            }
                        })

                        // Show notification badge if the latest message is not viewed
                        if (!response[0].viewed && response[0].senderId !== userId && !$('#noti').hasClass("notification-badge")) {
                            $('#noti').addClass("notification-badge");
                            $('#noti').text("!")
                        }
                        // Scroll to the bottom of the chat logs
                        $(".chat-logs").scrollTop($(".chat-logs")[0].scrollHeight);
                    }
                }
            });
        }

        // Initial load of messages
        loadMessages();

        // Function to create a message div
        function createDiv(msg, type, sendtime) {
            var chatmessage = $("<div>").attr({
                class: "chat-msg " + type,
            });
            var avatar = $("<span>").attr({
                class: "msg-avatar",
            });
            var img = $("<img>").attr({
                src: "<c:url value="/customer/images/logo/avatar-icon.jpg"/>",
            });
            avatar.append(img);
            var message = $("<div>").attr({
                class: "cm-msg-text d-flex flex-column",
            });
            var text = $("<div>").attr({
                class: "chat-text",
            })
            text.append(msg)
            message.append(text);
            var timeDiv = $("<div>").attr({
                class: "chat-time",
            })

            var date = new Date(sendtime);
            var time = formatTime(date,"hh:mm");
            timeDiv.append(time)
            message.append(timeDiv);
            chatmessage.append(avatar);
            chatmessage.append(message);
            return chatmessage;
        }

        // Event listener for chat logs scrolling
        $(".chat-logs").scroll(function () {
            if ($(this).scrollTop() === 0) {
                // Load more messages when scrolling to the top
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
                        if (response !== undefined) {
                            section++;
                            response.forEach(n => {
                                if (n.senderId == userId) {
                                    generateMessage(n.senderId, n.content, 'user', n.time, n.viewed, true);
                                } else {
                                    generateMessage(n.senderId, n.content, 'admin', n.time, n.viewed, true);
                                }
                            })
                            var positionToScroll = $(".chat-logs").find(".chat-msg").eq(response.length).position();
                            $(".chat-logs").scrollTop(positionToScroll.top);
                        }
                    }
                });
            } else if ($(this).scrollTop() > 0) {
            }
        });

        // Event listener for the chat submit button
        $("#chat-submit").click(function (e) {
            e.preventDefault();
            var msg = $("#chat-input").val();

            if (msg.trim() === '') {
                return false;
            }
            // Check if the WebSocket connection is open
            if (socket.readyState === WebSocket.OPEN) {
                // Send the message to the server via WebSocket
                var message = {msg: msg};
                socket.send(JSON.stringify(message));
                generateMessage(userId, msg, 'user', new Date().getTime(), false);
            } else {
                console.log("WebSocket is not in OPEN state.");
            }
            $("#chat-input").val("");
        });

        function formatTime(date, format) {
            var hours = date.getHours();
            var minutes = date.getMinutes();

            // Add a leading zero if the hour, minute, or second has only one digit
            hours = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;

            // Replace formatting elements
            format = format.replace("hh", hours);
            format = format.replace("mm", minutes);
            return format;
        }

        // Function to generate and display a chat message
        function generateMessage(sender, msg, type, time, viewed, pre) {
            if (pre) {
                $(".chat-logs").prepend(createDiv(msg, type, time));
                messages.unshift({senderId: sender, time: time, content: msg, viewed: viewed});
            } else {
                $(".chat-logs").append(createDiv(msg, type, time));
                messages.push({senderId: sender, time: time, content: msg, viewed: viewed});
                $(".chat-logs").stop().animate({scrollTop: $(".chat-logs")[0].scrollHeight}, 1000);
            }
        }

        // Event listener for chat circle click
        $("#chat-circle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
            $(".chat-logs").scrollTop($(".chat-logs")[0].scrollHeight);
            updateViewed();
            // Clear the notification badge
            if ($('#noti').hasClass("notification-badge")) {
                $('#noti').removeClass("notification-badge");
                $('#noti').text("")
            }
        })

        // Event listener for chat box toggle click
        $(".chat-box-toggle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
        })

    });

</script>