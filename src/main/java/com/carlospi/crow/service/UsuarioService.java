package com.carlospi.crow.service;

import com.carlospi.crow.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> obtenerPorId(Long id);
    List<Usuario> listarUsuarios();
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
    void eliminarUsuario(Long id);
    public List<Usuario> obtenerSeguidores(Long usuarioId);
    Optional<Usuario> obtenerPorEmail(String email);

    Optional<Usuario> obtenerPorUsuario(String usuario);
}

