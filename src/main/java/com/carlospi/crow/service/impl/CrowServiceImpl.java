package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.repository.CrowRepository;
import com.carlospi.crow.service.CrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrowServiceImpl implements CrowService {

    private final CrowRepository crowRepository;

    public CrowServiceImpl(CrowRepository crowRepository) {
        this.crowRepository = crowRepository;
    }

    @Override
    public Crow crearCrow(Crow crow) {
        return crowRepository.save(crow);
    }

    @Override
    public Optional<Crow> obtenerPorId(Long id) {
        return crowRepository.findById(id);
    }

    @Override
    public List<Crow> listarCrows() {
        return crowRepository.findAll();
    }

    @Override
    public Crow actualizarCrow(Long id, Crow crowActualizado) {
        Crow crow = crowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crow no encontrado"));

        crow.setTitulo(crowActualizado.getTitulo());
        crow.setDescripcion(crowActualizado.getDescripcion());
        crow.setMetaDonacion(crowActualizado.getMetaDonacion());
        crow.setRecaudado(crowActualizado.getRecaudado());
        crow.setVideoPromocional(crowActualizado.getVideoPromocional());
        crow.setImagenes(crowActualizado.getImagenes());
        crow.setFechaLimite(crowActualizado.getFechaLimite());
        crow.setCategoria(crowActualizado.getCategoria());

        return crowRepository.save(crow);
    }

    @Override
    public void eliminarCrow(Long id) {
        crowRepository.deleteById(id);
    }
}

