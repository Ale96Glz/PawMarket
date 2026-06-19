package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.ProductoRequestDTO;
import com.bluesoft.pawmarket.dto.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {
    ProductoResponseDTO crear(ProductoRequestDTO dto);
    ProductoResponseDTO buscarPorId(Long id);

    Page<ProductoResponseDTO> listar(Pageable pageable);

    List<ProductoResponseDTO> buscarPorNombre(String nombre);

    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto);

    void eliminar(Long id);
}
