package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Recompensa;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Long metaDonacion;
    private Long recaudado;
    private String videoPromocional;
    private String[] imagenes;
    private String[] galeriaMultimedia;
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private CategoriaCrowEnum categoria;
    private UsuarioResponseDTO usuario;
    private List<RecompensasResponseDTO> recompensas;

    public CrowResponseDTO(Crow crow) {
        this.id = crow.getId();
        this.titulo = crow.getTitulo();
        this.descripcion = crow.getDescripcion();
        this.metaDonacion = crow.getMetaDonacion();
        this.recaudado = crow.getRecaudado();
        this.videoPromocional = crow.getVideoPromocional();
        this.imagenes = crow.getImagenes().toArray(new String[0]);

        List<String> galeria = new ArrayList<>();
        if (crow.getVideoPromocional() != null && !crow.getVideoPromocional().isEmpty()) {
            galeria.add(crow.getVideoPromocional());
        }
        galeria.addAll(crow.getImagenes());
        this.galeriaMultimedia = galeria.toArray(new String[0]);

        this.fechaCreacion = crow.getFechaCreacion().toLocalDate();
        this.fechaLimite = crow.getFechaLimite();
        this.categoria = crow.getCategoria();
        this.usuario = new UsuarioResponseDTO(crow.getUsuario());
        this.recompensas = new ArrayList<>();
        if (crow.getRecompensas() != null) {
            for (Recompensa recompensa : crow.getRecompensas()) {
                this.recompensas.add(new RecompensasResponseDTO(recompensa));
            }
        }
    }
}
