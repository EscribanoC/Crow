package com.carlospi.crow.service;

import com.carlospi.crow.model.Administrador;

import java.util.List;
import java.util.Optional;

public interface AdministradorService {
    Administrador crearAdministrador(Administrador administrador);
    List<Administrador> listarAdministradores();
    Optional<Administrador> obtenerPorId(Long id);
    Administrador actualizarAdministrador(Long id, Administrador administrador);
    void eliminarAdministrador(Long id);
}

