package com.carlospi.crow.model;

import com.carlospi.crow.model.enumeration.GeneroEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String usuario;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    private String avatar;

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<Crow> crows = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "recompensa_usuario",
                joinColumns = @JoinColumn(name = "usuario_id"),
                inverseJoinColumns = @JoinColumn(name = "recompensa_id"))
    private List<Recompensa> recompensas = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "usuario_seguido",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_seguido_id"))
    private List<Usuario> usuariosSeguidos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "crow_seguido",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "crow_id"))
    private List<Crow> crowsSeguidos = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
