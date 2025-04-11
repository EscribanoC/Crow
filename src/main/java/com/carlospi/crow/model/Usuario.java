package com.carlospi.crow.model;

import com.carlospi.crow.model.enumeration.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String usuario;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotBlank
    private String avatar;

    @OneToMany(mappedBy = "usuario")
    private List<Crow> crows = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "recompensa_usuario",
                joinColumns = @JoinColumn(name = "usuario_id"),
                inverseJoinColumns = @JoinColumn(name = "recompensa_id"))
    private List<Recompensa> recompensas = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "usuario_favorito",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_favorito_id"))
    private List<Usuario> usuariosFavoritos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "crow_favorito",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "crow_id"))
    private List<Crow> crowsFavoritos = new ArrayList<>();
}
