package com.carlospi.crow.model;

import com.carlospi.crow.model.enumeration.CategoriaCrow;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Crow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    @Min(1)
    private Long metaDonacion;

    @NotNull
    private Long recaudado;

    private String videoPromocional;

    @ElementCollection
    private List<String> imagenes = new ArrayList<>();

    @NotNull
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @NotNull
    @FutureOrPresent
    private LocalDateTime fechaLimite;

    @Enumerated(EnumType.STRING)
    private CategoriaCrow categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "crow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recompensa> recompensas = new ArrayList<>();

    @OneToMany(mappedBy = "crow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donacion> donaciones = new ArrayList<>();
}
