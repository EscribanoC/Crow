package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Notificacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.TipoNotificacion;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.CrowService;
import com.carlospi.crow.service.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crows")
@RequiredArgsConstructor
public class CrowController {

    private final CrowService crowService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<Crow> crearCrow(@RequestBody Crow crow, HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        crow.setUsuario(usuario);
        Crow nuevoCrow = crowService.crearCrow(crow);

        List<Usuario> seguidores = usuarioRepository.findSeguidoresByUsuarioId(usuario.getId());
        for (Usuario seguidor : seguidores) {
            String mensaje = crow.getUsuario() + " ha creado un nuevo Crow: \"" + crow.getTitulo() + "\".";
            notificacionService.crearNotificacion(TipoNotificacion.NUEVO_CROW, mensaje, seguidor);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCrow);
    }

    @GetMapping
    public ResponseEntity<List<Crow>> listarCrows() {
        return ResponseEntity.ok(crowService.listarCrows());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Crow> actualizarCrow(@PathVariable Long id, @RequestBody Crow crowEditado, HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        Crow crowExistente = crowService.obtenerPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!crowExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Crow actualizado = crowService.actualizarCrow(id, crowEditado);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCrow(@PathVariable Long id, HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        Crow crow = crowService.obtenerPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!crow.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        crowService.eliminarCrow(id);
        return ResponseEntity.noContent().build();
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;
    }
}

