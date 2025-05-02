package com.carlospi.crow.repository;

import com.carlospi.crow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
/*
    @Query("SELECT u FROM Usuario u JOIN u.usuariosSeguidos f WHERE f.id = :usuarioId")
    List<Usuario> findSeguidoresByUsuarioId(Long id);*/
}
