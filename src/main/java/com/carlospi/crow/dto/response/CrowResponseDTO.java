package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private CategoriaCrowEnum categoria;
    private UsuarioResponseDTO usuario;

    public CrowResponseDTO(Crow crow) {
        this.id = crow.getId();
        this.titulo = crow.getTitulo();
        this.descripcion = crow.getDescripcion();
        this.metaDonacion = crow.getMetaDonacion();
        this.recaudado = crow.getRecaudado();
        this.videoPromocional = crow.getVideoPromocional();
        this.imagenes = crow.getImagenes().toArray(new String[0]);
        this.fechaCreacion = crow.getFechaCreacion().toLocalDate();
        this.fechaLimite = crow.getFechaLimite().toLocalDate();
        this.categoria = crow.getCategoria();
        this.usuario = new UsuarioResponseDTO(crow.getUsuario());
    }
}
