package com.carlospi.crow.service;

import com.carlospi.crow.dto.request.UsuarioRequestDTO;
import com.carlospi.crow.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> obtenerPorId(Long id);
    List<Usuario> listarUsuarios();
    void actualizarUsuario(Long id, UsuarioRequestDTO userResponse);
    void eliminarUsuario(Long id);
    public List<Usuario> obtenerSeguidores(Long usuarioId);
    Optional<Usuario> obtenerPorEmail(String email);
    Optional<Usuario> obtenerPorUsuario(String usuario);
    void dejarDeSeguirUsuario(Usuario seguidor, Long seguidoId);
    void seguirUsuario(Usuario seguidor, Long seguidoId);
}

