package com.carlospi.crow.dto.request;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowRequestDTO {
    private String titulo;
    private String descripcion;
    private Long metaDonacion;
    private Long recaudado;
    private String videoPromocional;
    private String[] imagenes;
    private LocalDateTime fechaLimite;
    private CategoriaCrowEnum categoria;
}
