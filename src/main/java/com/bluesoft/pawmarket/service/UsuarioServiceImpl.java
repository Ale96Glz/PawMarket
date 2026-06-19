package com.bluesoft.pawmarket.service;

import com.bluesoft.pawmarket.dto.UsuarioRequestDTO;
import com.bluesoft.pawmarket.dto.UsuarioResponseDTO;
import com.bluesoft.pawmarket.dto.UsuarioUpdateRequestDTO;
import com.bluesoft.pawmarket.entity.Usuario;
import com.bluesoft.pawmarket.exception.BusinessException;
import com.bluesoft.pawmarket.exception.ResourceNotFoundException;
import com.bluesoft.pawmarket.mapper.UsuarioMapper;
import com.bluesoft.pawmarket.repository.UsuarioRepository;
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
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByUsuario(dto.getUsuario())) {
            throw new BusinessException("Ya existe un usuario con ese nombre: " + dto.getUsuario());
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un usuario con ese email: " + dto.getEmail());
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No se encontró el usuario con id: " + id));

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre).stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioUpdateRequestDTO dto) {
        if (usuarioRepository.existsByUsuarioAndIdNot(dto.getUsuario(), id)) {
            throw new BusinessException("Ya existe otro usuario con ese nombre: " + dto.getUsuario());
        }
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Ya existe otro usuario con ese email: " + dto.getEmail());
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No se encontró el usuario con id: " + id));

        usuarioMapper.updateEntityFromDTO(dto, usuario);

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No se encontró el usuario con id: " + id));

        usuarioRepository.delete(usuario);
    }
}
