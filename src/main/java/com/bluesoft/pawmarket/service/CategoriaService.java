package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.CategoriaRequestDTO;
import com.bluesoft.pawmarket.dto.CategoriaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaService {

    CategoriaResponseDTO crear(CategoriaRequestDTO dto);

    CategoriaResponseDTO buscarPorId(Long id);

    Page<CategoriaResponseDTO> listar(Pageable pageable);

    List<CategoriaResponseDTO> buscarPorNombre(String nombre);

    CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto);

    void eliminar(Long id);
}
