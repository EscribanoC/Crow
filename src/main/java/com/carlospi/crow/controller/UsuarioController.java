package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.dto.request.UsuarioRequestDTO;
import com.carlospi.crow.dto.response.UsuarioResponseDTO;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByUsuario(HttpServletRequest request,
                                                                  @PathVariable String usuario) {
        JwtService jwtService = new JwtService();
        Usuario usuarioActual = null;
        if(request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            if (!token.isEmpty()) {
                String email = jwtService.extractUsername(token);
                usuarioActual = usuarioService.obtenerPorEmail(email).orElse(null);
            }
        }


        Optional<Usuario> u = usuarioService.obtenerPorUsuario(usuario);
        if (u.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Usuario user = u.get();

        UsuarioResponseDTO dto = new UsuarioResponseDTO(user);


        if(usuarioActual != null) {
            boolean isFollowing = usuarioActual.getUsuariosSeguidos().contains(user);
            dto.setFollowing(isFollowing);
            System.out.println("Usuario actual: " + usuarioActual.getUsuario() + ", siguiendo a: " + user.getUsuario() + " - " + isFollowing);
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getCurrentUsuario(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UsuarioResponseDTO dto = new UsuarioResponseDTO(usuario);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<Void> seguirUsuario(@AuthenticationPrincipal Usuario usuarioActual,
                                              @PathVariable Long id) {
        if (usuarioActual == null || usuarioActual.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        usuarioService.seguirUsuario(usuarioActual, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow/{id}")
    public ResponseEntity<Void> dejarDeSeguir(@AuthenticationPrincipal Usuario usuarioActual,
                                              @PathVariable Long id) {
        if (usuarioActual == null || usuarioActual.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        usuarioService.dejarDeSeguirUsuario(usuarioActual, id);
        return ResponseEntity.ok().build();
    }
}
