package com.commenau.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import javax.inject.Inject;

public abstract class GeneralMapper<D, E> {
	/**
	 * D: DTO; E: Entity
	 */
	@Inject
	private ModelMapper modelMapper;

	public List<D> toDTO(List<E> entities, Class<D> dtoClass) {
		List<D> result = new ArrayList<>();
		entities.stream().forEach(entity -> {
			D dto = modelMapper.map(entity, dtoClass);
			result.add(dto);
		});
		return result;
	}

	public D toDTO(E entity, Class<D> dtoClass) {
		D dto = modelMapper.map(entity, dtoClass);
		return dto;
	}


}
