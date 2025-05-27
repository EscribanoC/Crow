package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Recompensa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecompensasResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private Integer metaDonacion;

    public RecompensasResponseDTO(Recompensa recompensa) {
        this.id = recompensa.getId();
        this.titulo = recompensa.getTitulo();
        this.descripcion = recompensa.getDescripcion();
        this.imagen = recompensa.getImagen();
        this.metaDonacion = recompensa.getMetaDonacion();
    }
}
