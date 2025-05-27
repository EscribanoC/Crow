package com.carlospi.crow.controller.auth;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.model.enumeration.GeneroEnum;
import com.carlospi.crow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticationService service;
    private final UsuarioRepository usuarioRepository;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthenticationResponse> register(
            @RequestParam("email") String email,
            @RequestParam("usuario") String usuario,
            @RequestParam("password") String password,
            @RequestParam("genero") GeneroEnum genero,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        String avatarPath = null;

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadDir = "uploads/avatars/";
            String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                avatarPath = "avatars/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }


        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new AuthenticationResponse("Este correo ya está registrado", ""));
        }
        if (usuarioRepository.existsByUsuario(usuario)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new AuthenticationResponse("Este nombre de usuario ya está en uso", ""));
        }

        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .usuario(usuario)
                .password(password)
                .genero(genero)
                .avatar(avatarPath)
                .build();

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(usuario);
        return new AuthenticationResponse(jwtToken, usuario.getUsuario());
    }
}
