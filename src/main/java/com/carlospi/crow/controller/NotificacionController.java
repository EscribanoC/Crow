package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.model.Notificacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerMisNotificaciones(HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuario.getId());
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        Notificacion notificacion = notificacionService.obtenerNotificacionPorId(id);
        return ResponseEntity.ok(notificacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;
    }
}

