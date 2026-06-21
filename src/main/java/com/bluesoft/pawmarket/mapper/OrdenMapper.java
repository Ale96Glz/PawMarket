package com.bluesoft.pawmarket.mapper;

import com.bluesoft.pawmarket.dto.OrdenDetalleRequestDTO;
import com.bluesoft.pawmarket.dto.OrdenDetalleResponseDTO;
import com.bluesoft.pawmarket.dto.OrdenResponseDTO;
import com.bluesoft.pawmarket.entity.Orden;
import com.bluesoft.pawmarket.entity.OrdenDetalle;
import com.bluesoft.pawmarket.entity.Producto;
import com.bluesoft.pawmarket.entity.Usuario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrdenMapper {
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "usuarioNombre", source = "usuario.nombre")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "detalles", source = "detalles")
    OrdenResponseDTO toResponseDTO(Orden orden);

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    OrdenDetalleResponseDTO toDetallesResponseDTO(OrdenDetalle detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orden", source = "orden")
    @Mapping(target = "producto", source = "producto")
    @Mapping(target = "cantidad", source = "dto.cantidad")
    @Mapping(target = "precioUnitario", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    OrdenDetalle toDetalleEntity(OrdenDetalleRequestDTO dto, Producto producto, Orden orden);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    Orden toEntity(Usuario usuario);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Orden orden) {
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);
    }
}
