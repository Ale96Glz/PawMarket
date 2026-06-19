package com.bluesoft.pawmarket.mapper;

import com.bluesoft.pawmarket.dto.CategoriaRequestDTO;
import com.bluesoft.pawmarket.dto.CategoriaResponseDTO;
import com.bluesoft.pawmarket.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaRequestDTO dto);

    CategoriaResponseDTO toResponseDTO(Categoria categoria);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    void updateEntityFromDTO(CategoriaRequestDTO dto, @MappingTarget Categoria categoria);
}
