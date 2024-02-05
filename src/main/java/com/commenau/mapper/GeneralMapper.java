package com.commenau.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import javax.inject.Inject;

public abstract class GeneralMapper<D, E> {
	/**
	 * D: DTO; E: Entity
	 */
	@Inject
	private ModelMapper modelMapper;

	public List<D> toDTO(List<E> entities, Class<D> dtoClass) {
		return entities.stream().map(entity -> {
			return modelMapper.map(entity, dtoClass);
		}).collect(Collectors.toList());
	}

	public D toDTO(E entity, Class<D> dtoClass) {
		D dto = modelMapper.map(entity, dtoClass);
		return dto;
	}


}
