package com.carlospi.crow.controller.auth;

import com.carlospi.crow.model.enumeration.Genero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String usuario;
    private String password;
    private Genero genero;
    private String avatar;
}
