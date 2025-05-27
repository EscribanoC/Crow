package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Crow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowBasicResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    //private UsuarioBasicResponseDTO usuario;

    public CrowBasicResponseDTO(Crow crow) {
        this.id = crow.getId();
        this.titulo = crow.getTitulo();
        this.descripcion = crow.getDescripcion();
        //this.usuario = new UsuarioBasicResponseDTO(crow.getUsuario());
    }
}