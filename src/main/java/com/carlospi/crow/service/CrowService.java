package com.carlospi.crow.service;

import com.carlospi.crow.model.Crow;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrowService {
    public Crow crearCrow(Crow crow);
    Optional<Crow> obtenerPorId(Long id);
    List<Crow> listarCrows();
    Crow actualizarCrow(Long id, Crow crow);
    void eliminarCrow(Long id);
    public Crow actualizarCamposParciales(Crow crow, Map<String, Object> campos);
    List<Crow> obtenerCrowsPorUsuario(Long userId);
}
