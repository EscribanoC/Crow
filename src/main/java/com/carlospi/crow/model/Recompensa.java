package com.carlospi.crow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotBlank
    private String imagen;

    @NotNull
    private Integer metaDonacion;

    @ManyToOne
    @JoinColumn(name = "crow_id", nullable = false)
    private Crow crow;

    @ManyToMany(mappedBy = "recompensas")
    private List<Usuario> usuarios = new ArrayList<>();
}
