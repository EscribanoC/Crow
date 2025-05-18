package com.carlospi.crow.controller;

import com.carlospi.crow.dto.request.UsuarioRequestDTO;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRequestDTO> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> u = usuarioService.obtenerPorId(id);
        if (u.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Usuario usuario = u.get();
        UsuarioRequestDTO dto = new UsuarioRequestDTO(usuario);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioRequestDTO> getCurrentUsuario(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UsuarioRequestDTO dto = new UsuarioRequestDTO(usuario);
        return ResponseEntity.ok(dto);
    }
}
