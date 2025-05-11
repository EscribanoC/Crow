package com.carlospi.crow.controller.auth;

import com.carlospi.crow.model.enumeration.GeneroEnum;
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
    private GeneroEnum genero;
    private String avatar;
}
