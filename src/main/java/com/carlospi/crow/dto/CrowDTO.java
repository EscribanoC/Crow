package com.carlospi.crow.dto;

import com.carlospi.crow.model.Crow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowDTO {
    private String titulo;
    private String descripcion;
    private Long metaDonacion;
    private Long recaudado;
    private String videoPromocional;
    private String[] imagenes;
    private Long idUsuario;

    public CrowDTO(Crow crow) {
        this.titulo = crow.getTitulo();
        this.descripcion = crow.getDescripcion();
        this.metaDonacion = crow.getMetaDonacion();
        this.recaudado = crow.getRecaudado();
        this.videoPromocional = crow.getVideoPromocional();
        this.imagenes = crow.getImagenes().toArray(new String[0]);
        this.idUsuario = crow.getUsuario().getId();
    }
}
