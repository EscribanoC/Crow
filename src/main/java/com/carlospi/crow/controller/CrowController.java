package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.dto.request.CrowRequestDTO;
import com.carlospi.crow.dto.response.CrowResponseDTO;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.CrowService;
import com.carlospi.crow.service.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/crows")
@RequiredArgsConstructor
public class CrowController {

    private final CrowService crowService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<CrowResponseDTO>> listarCrows() {
        List<Crow> crows = crowService.listarCrows();

        if (crows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CrowResponseDTO> dto = crows.stream()
                .map(crow -> new CrowResponseDTO(crow))
                .toList();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<CrowResponseDTO> crearCrow(@RequestBody CrowRequestDTO crowRequestdto, HttpServletRequest request) {
        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Crow crow = new Crow();

        ModelMapper mapper = new ModelMapper();
        mapper.map(crowRequestdto, crow);

        crow.setUsuario(usuario);
        crow.setFechaCreacion(LocalDateTime.now());
        crow.setRecompensas(null);
        crow.setDonaciones(null);

        CrowResponseDTO crowResponsedto = new CrowResponseDTO(crow);
        
        crowService.crearCrow(crow);
        /*
        List<Usuario> seguidores = usuarioRepository.findSeguidoresByUsuarioId(usuario.getId());
        for (Usuario seguidor : seguidores) {
            String mensaje = crow.getUsuario() + " ha creado un nuevo Crow: \"" + crow.getTitulo() + "\".";
            notificacionService.crearNotificacion(TipoNotificacion.NUEVO_CROW, mensaje, seguidor);
        }*/

        return ResponseEntity.status(HttpStatus.CREATED).body(crowResponsedto);
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
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

    @GetMapping("/crowOfTheWeek")
    public ResponseEntity<CrowResponseDTO> crowOfTheWeek() {
        List<Crow> crows = crowService.listarCrows();

        if (crows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Crow crow = crows.stream()
                .filter(c -> c.getMetaDonacion() != null && c.getMetaDonacion() > 0)
                .max((c1, c2) -> {
                    double progress1 = (double) c1.getRecaudado() / c1.getMetaDonacion();
                    double progress2 = (double) c2.getRecaudado() / c2.getMetaDonacion();
                    return Double.compare(progress1, progress2);
                })
                .orElse(null);

        if (crow == null) {
            return ResponseEntity.noContent().build();
        }

        CrowResponseDTO crowResponseDTO = new CrowResponseDTO(crow);
        return ResponseEntity.ok(crowResponseDTO);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;
    }
}

