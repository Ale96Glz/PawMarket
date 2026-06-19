package com.bluesoft.pawmarket.controller;

import com.bluesoft.pawmarket.dto.CategoriaRequestDTO;
import com.bluesoft.pawmarket.dto.CategoriaResponseDTO;
import com.bluesoft.pawmarket.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categoria")
@Slf4j
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(
            @Valid @RequestBody CategoriaRequestDTO dto
            )
    {
        CategoriaResponseDTO creado = categoriaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtener(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto
    ){
        return ResponseEntity.ok(categoriaService.actualizar(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ){
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> listar(
         @ParameterObject @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable
    )
    {
        return ResponseEntity.ok(categoriaService.listar(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponseDTO>> buscar(
            @RequestParam String nombre
    ){
        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
    }
}
