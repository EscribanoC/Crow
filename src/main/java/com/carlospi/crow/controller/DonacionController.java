package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Donacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.TipoNotificacionEnum;
import com.carlospi.crow.repository.CrowRepository;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.DonacionService;
import com.carlospi.crow.service.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donaciones")
@RequiredArgsConstructor
public class DonacionController {

    private final DonacionService donacionService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final CrowRepository crowRepository;
    private final NotificacionService notificacionService;

    @PostMapping("/{crowId}")
    public ResponseEntity<Donacion> donar(
            @PathVariable Long crowId,
            @RequestParam Long cantidad,
            HttpServletRequest request
    ) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        Crow crow = crowRepository.findById(crowId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crow no encontrado"));

        Donacion donacion = new Donacion();
        donacion.setCantidad(cantidad);
        donacion.setCrow(crow);
        donacion.setUsuario(usuario);

        Donacion donacionCreada = donacionService.crearDonacion(donacion);

        String mensaje = usuario.getUsuario() + " ha donado " + cantidad + "€ a tu Crow \"" + crow.getTitulo() + "\".";
        notificacionService.crearNotificacion(TipoNotificacionEnum.DONACION, mensaje, crow.getUsuario());

        return ResponseEntity.status(HttpStatus.CREATED).body(donacionCreada);
    }

    @GetMapping("/mis-donaciones")
    public ResponseEntity<List<Donacion>> obtenerDonacionesUsuario(HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        List<Donacion> donaciones = donacionService.obtenerDonacionesPorUsuario(usuario.getId());
        return ResponseEntity.ok(donaciones);
    }

    @GetMapping("/crow/{crowId}")
    public ResponseEntity<List<Donacion>> obtenerDonacionesPorCrow(@PathVariable Long crowId) {
        List<Donacion> donaciones = donacionService.obtenerDonacionesPorCrow(crowId);
        return ResponseEntity.ok(donaciones);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;
    }
}
