package com.bluesoft.pawmarket.mapper;

import com.bluesoft.pawmarket.dto.ProductoRequestDTO;
import com.bluesoft.pawmarket.dto.ProductoResponseDTO;
import com.bluesoft.pawmarket.entity.Categoria;
import com.bluesoft.pawmarket.entity.Producto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombre", source = "dto.nombre")
    @Mapping(target = "precio", source = "dto.precio")
    @Mapping(target = "descripcion", source = "dto.descripcion")
    @Mapping(target = "cantidad", source = "dto.cantidad")
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "estadoProducto", ignore = true)
    Producto toEntity(ProductoRequestDTO dto, Categoria categoria);

    @Mapping(target = "categoria", source = "categoria.nombre")
    @Mapping(target = "estado", source = "estadoProducto")
    ProductoResponseDTO toResponseDTO(Producto producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombre", source = "dto.nombre")
    @Mapping(target = "precio", source = "dto.precio")
    @Mapping(target = "descripcion", source = "dto.descripcion")
    @Mapping(target = "cantidad", source = "dto.cantidad")
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "estadoProducto", ignore = true)
    void updateEntityFromDTO(ProductoRequestDTO dto, @MappingTarget Producto producto, Categoria categoria);

    @AfterMapping
    default void defaultsAlCrear(@MappingTarget Producto producto, ProductoRequestDTO dto) {
        if (producto.getEstadoProducto() == null) {
            producto.setEstadoProducto(Producto.EstadoProducto.ACTIVO);
        }
    }
}
