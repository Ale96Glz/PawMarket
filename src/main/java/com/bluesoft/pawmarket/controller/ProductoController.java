package com.bluesoft.pawmarket.controller;


import com.bluesoft.pawmarket.dto.ProductoRequestDTO;
import com.bluesoft.pawmarket.dto.ProductoResponseDTO;
import com.bluesoft.pawmarket.exception.ResourceNotFoundException;
import com.bluesoft.pawmarket.mapper.ProductoMapper;
import com.bluesoft.pawmarket.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/producto")
@Slf4j
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody ProductoRequestDTO dto
    ) {

        ProductoResponseDTO creado = productoService.crear(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtener(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listar(
            @ParameterObject @PageableDefault(size = 20, sort = "nombre", direction = Sort.Direction.ASC)
            Pageable pageable
            )
    {
        return ResponseEntity.ok(productoService.listar(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscar(
      @RequestParam String nombre
    ){
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar (
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity.ok(productoService.actualizar(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (
            @PathVariable Long id
    ){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
