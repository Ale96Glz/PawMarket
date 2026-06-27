package com.bluesoft.pawmarket.controller;


import com.bluesoft.pawmarket.dto.OrdenEstadoRequestDTO;
import com.bluesoft.pawmarket.dto.OrdenRequestDTO;
import com.bluesoft.pawmarket.dto.OrdenResponseDTO;
import com.bluesoft.pawmarket.entity.Orden;
import com.bluesoft.pawmarket.service.OrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orden")
@Slf4j
public class OrdenController {
    private final OrdenService ordenService;

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> obtener(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrdenResponseDTO>> listar
            (
            @ParameterObject @PageableDefault(size = 20, sort = "creadoEn", direction = Sort.Direction.ASC)
            Pageable pageable
            )
    {
        return ResponseEntity.ok(ordenService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crear(
            @Valid @RequestBody OrdenRequestDTO dto
    ){
      OrdenResponseDTO creado = ordenService.crear(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenResponseDTO> cambiarEstado(
        @PathVariable Long id,
        @Valid @RequestBody OrdenEstadoRequestDTO dto
    ){
    return ResponseEntity.ok(ordenService.cambiarEstado(id, dto.getEstado()));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<OrdenResponseDTO>> listarPorUsuario(
        @PathVariable Long usuarioId,
        @ParameterObject @PageableDefault(size = 20, sort = "creadoEn", direction = Sort.Direction.DESC)
        Pageable pageable
    ){
    return ResponseEntity.ok(ordenService.listarPorUsuario(usuarioId, pageable));
    }

}
