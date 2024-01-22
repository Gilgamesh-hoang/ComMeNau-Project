package com.commenau.mapper;

import com.commenau.dto.ContactDTO;
import com.commenau.dto.ProductDTO;
import com.commenau.model.Contact;
import com.commenau.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {
    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDTO toDTO(Contact contact);
}
