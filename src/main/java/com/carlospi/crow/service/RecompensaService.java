package com.carlospi.crow.service;

import com.carlospi.crow.model.Recompensa;

import java.util.List;
import java.util.Optional;

public interface RecompensaService {
    Recompensa crearRecompensa(Recompensa recompensa);
    Optional<Recompensa> obtenerPorId(Long id);
    List<Recompensa> listarRecompensas();
    Recompensa actualizarRecompensa(Long id, Recompensa recompensa);
    void eliminarRecompensa(Long id);
}


