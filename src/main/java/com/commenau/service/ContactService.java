package com.commenau.service;

import com.commenau.dao.ContactDAO;
import com.commenau.dto.ContactDTO;
import com.commenau.mapper.ContactMapper;
import com.commenau.model.Contact;
import com.commenau.model.ReplyContact;
import com.commenau.pagination.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

public class ContactService {
    @Inject
    private ContactDAO contactDAO;
    @Inject
    private ContactMapper mapper;
    public boolean insert(Contact contact) {
        return contactDAO.save(contact);
    }

    public int countAll() {
        return contactDAO.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return countAll();
        return contactDAO.countByKeyWord(keyWord);
    }

    public List<ContactDTO> getAll(PageRequest pageRequest) {
        List<ContactDTO> list = contactDAO.findAll(pageRequest);
        list.forEach(contact -> {
            String message = contact.getMessage();
            // display description only 300 character
            if (message.length() > 300) {
                int lastSpaceLast = message.indexOf(" ", 300);
                message = message.substring(0, lastSpaceLast) + ".....";
                contact.setMessage(message);
            }
        });
        return list;
    }


    public List<ContactDTO> getByKeyWord(PageRequest pageRequest, String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return getAll(pageRequest);
        else
            return contactDAO.findByKeyWord(pageRequest, keyWord);
    }

    public ContactDTO getById(int contactId) {
        Contact contact = contactDAO.findOneById(contactId);
        ContactDTO result = mapper.toDTO(contact, ContactDTO.class);
        result.setReply(contactDAO.findReplyByContactId(contact.getId()));
        return result;
    }

    public boolean deleteByIds(Integer[] ids) {
        try {
            for (Integer id : ids) {
                //delete contact's reply
                contactDAO.deleteReplyByContactId(id);
                //delete contact
                contactDAO.deleteById(id);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveReply(ReplyContact replyContact) {
        return contactDAO.saveReply(replyContact);
    }


}
