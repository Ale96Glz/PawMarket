package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.OrdenRequestDTO;
import com.bluesoft.pawmarket.dto.OrdenResponseDTO;
import com.bluesoft.pawmarket.entity.Orden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdenService {
    OrdenResponseDTO crear(OrdenRequestDTO dto);
    OrdenResponseDTO buscarPorId(Long id);
    Page<OrdenResponseDTO> listar(Pageable pageable);
    Page<OrdenResponseDTO> listarPorUsuario (Long usuarioId, Pageable pageable);
    OrdenResponseDTO cambiarEstado(Long id, Orden.EstadoOrden estadoOrden);
}
