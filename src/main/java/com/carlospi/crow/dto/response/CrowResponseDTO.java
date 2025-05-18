package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowResponseDTO {
    private String titulo;
    private String descripcion;
    private Long metaDonacion;
    private Long recaudado;
    private String videoPromocional;
    private String[] imagenes;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLimite;
    private CategoriaCrowEnum categoria;
    private UsuarioResponseDTO usuario;

    public CrowResponseDTO(Crow crow) {
        this.titulo = crow.getTitulo();
        this.descripcion = crow.getDescripcion();
        this.metaDonacion = crow.getMetaDonacion();
        this.recaudado = crow.getRecaudado();
        this.videoPromocional = crow.getVideoPromocional();
        this.imagenes = crow.getImagenes().toArray(new String[0]);
        this.fechaCreacion = crow.getFechaCreacion();
        this.fechaLimite = crow.getFechaLimite();
        this.categoria = crow.getCategoria();
        this.usuario = new UsuarioResponseDTO(crow.getUsuario());
    }
}
