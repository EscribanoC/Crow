package com.carlospi.crow.service.impl;

import com.carlospi.crow.controller.CrowController;
import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.enumeration.CategoriaCrowEnum;
import com.carlospi.crow.repository.CrowRepository;
import com.carlospi.crow.service.CrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    public Crow actualizarCamposParciales(Crow crow, Map<String, Object> campos) {
        if (campos.containsKey("titulo")) {
            crow.setTitulo((String) campos.get("titulo"));
        }
        if (campos.containsKey("descripcion")) {
            crow.setDescripcion((String) campos.get("descripcion"));
        }
        if (campos.containsKey("metaDonacion")) {
            crow.setMetaDonacion(Long.valueOf(campos.get("metaDonacion").toString()));
        }
        if (campos.containsKey("recaudado")) {
            crow.setRecaudado(Long.valueOf(campos.get("recaudado").toString()));
        }
        if (campos.containsKey("fechaLimite")) {
            crow.setFechaLimite(LocalDate.parse((String) campos.get("fechaLimite")));
        }
        if (campos.containsKey("categoria")) {
            try {
                String categoriaStr = campos.get("categoria").toString();
                CategoriaCrowEnum categoriaEnum = CategoriaCrowEnum.valueOf(categoriaStr.toUpperCase());
                crow.setCategoria(categoriaEnum);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no válida");
            }
        }

        return crowRepository.save(crow);
    }

    @Override
    public List<Crow> obtenerCrowsPorUsuario(Long userId) {
        return crowRepository.findByUsuarioId(userId);
    }
}

