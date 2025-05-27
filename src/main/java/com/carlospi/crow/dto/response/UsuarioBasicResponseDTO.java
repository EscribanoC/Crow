package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioBasicResponseDTO {
    private Long id;
    private String usuario;
    private String avatar;

    public UsuarioBasicResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.usuario = usuario.getUsuario();
        this.avatar = usuario.getAvatar();
    }
}
