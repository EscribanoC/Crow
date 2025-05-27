package com.carlospi.crow.controller;

import com.carlospi.crow.config.JwtService;
import com.carlospi.crow.dto.request.CrowRequestDTO;
import com.carlospi.crow.dto.response.CrowResponseDTO;
import com.carlospi.crow.dto.response.RecompensasResponseDTO;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Recompensa;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import com.carlospi.crow.model.enumeration.TipoNotificacionEnum;
import com.carlospi.crow.repository.UsuarioRepository;
import com.carlospi.crow.service.CrowService;
import com.carlospi.crow.service.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CrowResponseDTO> crearCrow(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "videoPresentacion", required = false) MultipartFile video,
            @RequestParam(value = "imagenesGaleria", required = false) List<MultipartFile> imagenes,
            @RequestParam("meta") Long meta,
            @RequestParam("categoria") String categoria,
            @RequestParam("fechaLimite") LocalDate fechaLimite,
            HttpServletRequest request) {

        String email = jwtService.extractUsername(getToken(request));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Crow crow = new Crow();
        crow.setTitulo(titulo);
        crow.setDescripcion(descripcion);
        crow.setUsuario(usuario);
        crow.setFechaCreacion(LocalDateTime.now());
        crow.setMetaDonacion(meta);
        crow.setRecaudado(0L);
        crow.setCategoria(CategoriaCrowEnum.valueOf(categoria.toUpperCase()));
        crow.setFechaLimite(fechaLimite);

        List<Recompensa> recompensas = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String tituloR = request.getParameter("recompensas[" + i + "][titulo]");
            String descripcionR = request.getParameter("recompensas[" + i + "][descripcion]");
            String metaDonacionStr = request.getParameter("recompensas[" + i + "][metaDonacion]");
            MultipartFile imagenR = null;

            if (request instanceof MultipartHttpServletRequest multipartRequest) {
                imagenR = multipartRequest.getFile("recompensas[" + i + "][imagen]");
            }

            if (tituloR == null || descripcionR == null || metaDonacionStr == null) {
                continue;
            }

            int metaDonacion = Integer.parseInt(metaDonacionStr);

            String rutaImagen = "";
            if (imagenR != null && !imagenR.isEmpty()) {
                String imgPath = saveFile(imagenR, "uploads/recompensas/");
                rutaImagen = "recompensas/" + imgPath;
            }

            Recompensa recompensa = new Recompensa();
            recompensa.setTitulo(tituloR);
            recompensa.setDescripcion(descripcionR);
            recompensa.setMetaDonacion(metaDonacion);
            recompensa.setImagen(rutaImagen);
            recompensa.setCrow(crow);

            recompensas.add(recompensa);
        }

        crow.setRecompensas(recompensas);


        if (video != null && !video.isEmpty()) {
            String videoPath = saveFile(video, "uploads/crow-gallery/");
            crow.setVideoPromocional("crow-gallery/" + videoPath);
        }

        List<String> rutasImagenes = new ArrayList<>();
        if (imagenes != null) {
            for (MultipartFile img : imagenes) {
                if (!img.isEmpty()) {
                    String imgPath = saveFile(img, "uploads/crow-gallery/");
                    rutasImagenes.add("crow-gallery/" + imgPath);
                }
            }
        }
        crow.setImagenes(rutasImagenes);

        crowService.crearCrow(crow);

        List<Usuario> seguidores = usuario.getSeguidores();
        for (Usuario seguidor : seguidores) {
            notificacionService.crearNotificacion(
                    TipoNotificacionEnum.NUEVO_CROW,
                    seguidor,
                    usuario,
                    crow
            );
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(new CrowResponseDTO(crow));
    }

    private String saveFile(MultipartFile file, String uploadDir) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
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

    @GetMapping("/{id}")
    public ResponseEntity<CrowResponseDTO> obtenerCrowPorId(@PathVariable Long id) {
        Crow crow = crowService.obtenerPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        CrowResponseDTO crowResponseDTO = new CrowResponseDTO(crow);
        return ResponseEntity.ok(crowResponseDTO);
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

    @GetMapping("/{id}/rewards")
    public ResponseEntity<List<RecompensasResponseDTO>> obtenerRecompensasPorCrow(@PathVariable Long id) {
        Crow crow = crowService.obtenerPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Recompensa> recompensas = crow.getRecompensas();

        if (recompensas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<RecompensasResponseDTO> recompensasResponseDTO = recompensas.stream()
                .map(RecompensasResponseDTO::new)
                .toList();

        return ResponseEntity.ok(recompensasResponseDTO);
    }

    @GetMapping("/crowsByUser/{userId}")
    public ResponseEntity<List<CrowResponseDTO>> obtenerCrowsPorUsuario(@PathVariable Long userId) {
        List<Crow> crows = crowService.obtenerCrowsPorUsuario(userId);

        if (crows.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CrowResponseDTO> crowResponseDTOs = crows.stream()
                .map(CrowResponseDTO::new)
                .toList();

        return ResponseEntity.ok(crowResponseDTOs);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;
    }
}

