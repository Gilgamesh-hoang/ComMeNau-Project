package com.commenau.controller.customer;

import com.commenau.dto.MessageDTO;
import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.service.ConversationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/message")
public class MessageController extends HttpServlet {
    @Inject
    private ConversationService conversationService;
    private static final int numberMessagesPerSection = 8;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int participantId = Integer.parseInt(req.getParameter("participantId"));
            int section = Integer.parseInt(req.getParameter("section"));

            //paging
            PageRequest pageRequest = PageRequest.builder().page(section).maxPageItem(numberMessagesPerSection)
                    .sorters(List.of(new Sorter("sendTime", "DESC"))).build();

            List<MessageDTO> messages = conversationService.getMessages(participantId, pageRequest);
            resp.setContentType("application/json");
            resp.getWriter().write(new ObjectMapper().writeValueAsString(messages));
            resp.setStatus(!messages.isEmpty() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonData = mapper.readValue(req.getReader(), new TypeReference<Map<String, Object>>() {});

            int participantId = Integer.parseInt(jsonData.get("participantId").toString());
            int ownerId = Integer.parseInt(jsonData.get("ownerId").toString());

            boolean result = conversationService.updateViewed(participantId, ownerId);
            resp.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
