package com.carlospi.crow.model;

import com.carlospi.crow.model.enumeration.TipoNotificacionEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoNotificacionEnum tipo;

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Usuario receptor;

    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name = "crow_id", nullable = true)
    private Crow crow;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private boolean leida = false;
}
