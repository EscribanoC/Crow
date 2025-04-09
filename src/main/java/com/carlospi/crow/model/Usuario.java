package com.carlospi.crow.model;

import com.carlospi.crow.model.enumeration.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotBlank
    private String avatar;

    @OneToMany(mappedBy = "usuario")
    private List<Crow> crows = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "usuario_recompensa",
                joinColumns = @JoinColumn(name = "usuario_id"),
                inverseJoinColumns = @JoinColumn(name = "recompensa_id"))
    private List<Recompensa> recompensas = new ArrayList<>();
}
