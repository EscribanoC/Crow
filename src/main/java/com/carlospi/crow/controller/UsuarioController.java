package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.dto.request.UsuarioRequestDTO;
import com.carlospi.crow.dto.response.CrowResponseDTO;
import com.carlospi.crow.dto.response.UsuarioResponseDTO;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.GeneroEnum;
import com.carlospi.crow.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuarios (){
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UsuarioResponseDTO> dto = usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(usuario))
                .toList();

        return ResponseEntity.ok(dto);
    }

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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestParam("email") String email,
            @RequestParam("usuario") String usuario,
            @RequestParam("genero") GeneroEnum genero,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        Usuario u = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        u.setEmail(email);
        u.setUsuario(usuario);
        u.setGenero(genero);

        if (avatarFile != null && !avatarFile.isEmpty()) {
            System.out.println("-------------------------Avatar recibido-------------------------");
            String uploadDir = "uploads/avatars/";
            String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                u.setAvatar("avatars/" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el avatar");
            }
        }

        usuarioService.actualizarUsuario(id, new UsuarioRequestDTO(u));

        return ResponseEntity.ok().build();
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
