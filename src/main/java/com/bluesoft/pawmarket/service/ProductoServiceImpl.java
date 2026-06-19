package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.ProductoRequestDTO;
import com.bluesoft.pawmarket.dto.ProductoResponseDTO;
import com.bluesoft.pawmarket.entity.Categoria;
import com.bluesoft.pawmarket.entity.Producto;
import com.bluesoft.pawmarket.exception.BusinessException;
import com.bluesoft.pawmarket.exception.ResourceNotFoundException;
import com.bluesoft.pawmarket.mapper.ProductoMapper;
import com.bluesoft.pawmarket.repository.CategoriaRepository;
import com.bluesoft.pawmarket.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        log.info("Iniciando la creación del producto: {}", dto.getNombre());

        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe un producto con ese nombre: " + dto.getNombre());
        }

        Categoria categoria = resolverCategoria(dto.getCategoriaId());
        Producto producto = productoMapper.toEntity(dto, categoria);
        Producto guardado = productoRepository.save(producto);

        log.info("Se ha guardado el producto: {}", guardado.getId());

        return productoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        log.debug("Buscando el producto con id: {}", id);

        Producto producto = productoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No se encontró el producto con id: " + id));

        return productoMapper.toResponseDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listar(Pageable pageable) {
        return productoRepository.findAll(pageable).map(productoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre).stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        log.info("Actualizando el producto con id: {}", id);

        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No se encontró el producto con id: " + id));

        Categoria categoria = resolverCategoria(dto.getCategoriaId());
        productoMapper.updateEntityFromDTO(dto, producto, categoria);

        return productoMapper.toResponseDTO(producto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando el producto con id: {}", id);

        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el producto con id: " + id);
        }

        productoRepository.deleteById(id);

        log.info("El producto con id {} fue eliminado correctamente", id);
    }

    private Categoria resolverCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", categoriaId));
    }
}
