package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.CategoriaRequestDTO;
import com.bluesoft.pawmarket.dto.CategoriaResponseDTO;
import com.bluesoft.pawmarket.entity.Categoria;
import com.bluesoft.pawmarket.exception.BusinessException;
import com.bluesoft.pawmarket.exception.ResourceNotFoundException;
import com.bluesoft.pawmarket.mapper.CategoriaMapper;
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
public class CategoriaServiceImpl implements CategoriaService{

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional
    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        if(categoriaRepository.existsByNombre(dto.getNombre())){
            throw new BusinessException("Ya existe una categoría con ese nombre: " + dto.getNombre());
        }

        Categoria categoria = categoriaMapper.toEntity(dto);

        Categoria guardado = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {

        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No se encontró una categoría con id: " + id));

        return categoriaMapper.toResponseDTO(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(categoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre).stream().map(categoriaMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {

        if (categoriaRepository.existsByNombreAndIdNot(dto.getNombre(), id)){
            throw new BusinessException("No se puede actualizar la categoría porque ya existe otra con el mismo nombre: " +
                    dto.getNombre());
        }

        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No se encontró una categoría con id: " + id));

        categoriaMapper.updateEntityFromDTO(dto, categoria);

        return categoriaMapper.toResponseDTO(categoria);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Categoria categoria = categoriaRepository.findById(id).orElseThrow( ()
                -> new ResourceNotFoundException("No se encontró la categoría con id: " + id));

        if (productoRepository.existsByCategoria_Id(id)){
            throw new BusinessException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
    }
}
