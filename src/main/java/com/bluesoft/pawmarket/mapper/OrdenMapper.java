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
    OrdenResponseDTO toResponseDTO(Orden orden);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", source = "cliente")
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    Orden toEntity(Usuario cliente);

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    OrdenDetalleResponseDTO toDetalleResponseDTO(OrdenDetalle detalle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cantidad", source = "dto.cantidad")
    @Mapping(target = "precioUnitario", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    OrdenDetalle toDetalleEntity(OrdenDetalleRequestDTO dto, Producto producto, Orden orden);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Orden orden) {
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);
    }
}
