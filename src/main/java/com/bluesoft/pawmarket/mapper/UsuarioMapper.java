package com.bluesoft.pawmarket.mapper;

import com.bluesoft.pawmarket.dto.UsuarioRequestDTO;
import com.bluesoft.pawmarket.dto.UsuarioResponseDTO;
import com.bluesoft.pawmarket.dto.UsuarioUpdateRequestDTO;
import com.bluesoft.pawmarket.entity.Usuario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "rol", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateRequestDTO dto, @MappingTarget Usuario usuario);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Usuario usuario, UsuarioRequestDTO dto) {
        usuario.setActivo(true);
        usuario.setRol(Usuario.RolUsuario.CLIENTE);
    }
}
