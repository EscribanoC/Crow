package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setUsuario(usuarioActualizado.getUsuario());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setGenero(usuarioActualizado.getGenero());
        usuarioExistente.setAvatar(usuarioActualizado.getAvatar());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> obtenerSeguidores(Long usuarioId) {
        return null;
        //return usuarioRepository.findSeguidoresByUsuarioId(usuarioId);
    }

    @Override
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}

