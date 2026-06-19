package com.bluesoft.pawmarket.repository;

import com.bluesoft.pawmarket.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNombre(String nombre);

    List<Usuario> findByRol(Usuario.RolUsuario rol);

    List<Usuario> findByActivo(boolean activo);

    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    boolean existsByUsuarioAndIdNot(String usuario, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
