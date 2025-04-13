package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Administrador;
import com.carlospi.crow.repository.AdministradorRepository;
import com.carlospi.crow.service.AdministradorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorServiceImpl(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public Administrador crearAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    @Override
    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    @Override
    public Optional<Administrador> obtenerPorId(Long id) {
        return administradorRepository.findById(id);
    }

    @Override
    public Administrador actualizarAdministrador(Long id, Administrador administradorActualizado) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        administrador.setEmail(administradorActualizado.getEmail());
        administrador.setPassword(administradorActualizado.getPassword());

        return administradorRepository.save(administrador);
    }

    @Override
    public void eliminarAdministrador(Long id) {
        administradorRepository.deleteById(id);
    }
}
