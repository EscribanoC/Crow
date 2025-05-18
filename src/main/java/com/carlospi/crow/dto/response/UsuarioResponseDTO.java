package com.carlospi.crow.dto.response;

import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.GeneroEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String email;
    private String usuario;
    private String avatar;
    private GeneroEnum genero;

    public UsuarioResponseDTO(Usuario usuario) {
        this.email = usuario.getEmail();
        this.usuario = usuario.getUsuario();
        this.avatar = usuario.getAvatar();
        this.genero = usuario.getGenero();
    }
}