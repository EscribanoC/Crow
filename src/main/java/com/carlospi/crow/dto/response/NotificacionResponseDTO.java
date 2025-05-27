package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Notificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {
    private Long id;
    private String tipo;
    private UsuarioBasicResponseDTO receptor;
    private UsuarioBasicResponseDTO emisor;
    private CrowBasicResponseDTO crow;
    private String fechaCreacion;
    private boolean leida;

    public NotificacionResponseDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.tipo = notificacion.getTipo().name();
        this.receptor = new UsuarioBasicResponseDTO(notificacion.getReceptor());
        this.emisor = new UsuarioBasicResponseDTO(notificacion.getEmisor());
        this.crow = notificacion.getCrow() != null ? new CrowBasicResponseDTO(notificacion.getCrow()) : null;
        this.fechaCreacion = notificacion.getFechaCreacion().toString();
        this.leida = notificacion.isLeida();
    }
}
