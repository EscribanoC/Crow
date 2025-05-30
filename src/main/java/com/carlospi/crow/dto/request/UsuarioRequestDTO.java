package com.carlospi.crow.dto.request;

import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.GeneroEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String usuario;
    @NotBlank
    private GeneroEnum genero;
    private String avatar;

    public UsuarioRequestDTO(Usuario usuario) {
        this.email = usuario.getEmail();
        this.usuario = usuario.getUsuario();
        this.genero = usuario.getGenero();
        this.avatar = usuario.getAvatar();
    }
}