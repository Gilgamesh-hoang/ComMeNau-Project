package com.commenau.service;

import com.commenau.dao.ProductDAO;
import com.commenau.dto.ProductDTO;
import com.commenau.mapper.ProductMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductMapper productMapper;

    public List<ProductDTO> search(String query) {
        return productDAO.searchByName(query).stream().map(product ->
                productMapper.toDTO(product, ProductDTO.class)).collect(Collectors.toList());
    }

}
