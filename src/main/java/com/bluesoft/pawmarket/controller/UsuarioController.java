package com.bluesoft.pawmarket.controller;

import com.bluesoft.pawmarket.dto.UsuarioRequestDTO;
import com.bluesoft.pawmarket.dto.UsuarioResponseDTO;
import com.bluesoft.pawmarket.dto.UsuarioUpdateRequestDTO;
import com.bluesoft.pawmarket.service.UsuarioService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
@Slf4j
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @Valid @RequestBody UsuarioRequestDTO dto
    ){
        UsuarioResponseDTO creado = usuarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtener(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(
            @ParameterObject @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok(usuarioService.listar(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscar(
            @RequestParam String nombre
    ){
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequestDTO dto
    ){
            return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ){
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
