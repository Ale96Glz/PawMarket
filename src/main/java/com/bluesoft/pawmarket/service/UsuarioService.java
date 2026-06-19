package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.UsuarioRequestDTO;
import com.bluesoft.pawmarket.dto.UsuarioResponseDTO;
import com.bluesoft.pawmarket.dto.UsuarioUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO dto);

    UsuarioResponseDTO buscarPorId(Long id);

    Page<UsuarioResponseDTO> listar(Pageable pageable);

    List<UsuarioResponseDTO> buscarPorNombre(String nombre);

    UsuarioResponseDTO actualizar(Long id, UsuarioUpdateRequestDTO dto);

    void eliminar(Long id);
}
