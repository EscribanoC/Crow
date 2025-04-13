package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Recompensa;
import com.carlospi.crow.repository.RecompensaRepository;
import com.carlospi.crow.service.RecompensaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecompensaServiceImpl implements RecompensaService {

    private final RecompensaRepository recompensaRepository;

    public RecompensaServiceImpl(RecompensaRepository recompensaRepository) {
        this.recompensaRepository = recompensaRepository;
    }

    @Override
    public Recompensa crearRecompensa(Recompensa recompensa) {
        return recompensaRepository.save(recompensa);
    }

    @Override
    public Optional<Recompensa> obtenerPorId(Long id) {
        return recompensaRepository.findById(id);
    }

    @Override
    public List<Recompensa> listarRecompensas() {
        return recompensaRepository.findAll();
    }

    @Override
    public Recompensa actualizarRecompensa(Long id, Recompensa recompensaActualizada) {
        Recompensa recompensa = recompensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recompensa no encontrada"));

        recompensa.setTitulo(recompensaActualizada.getTitulo());
        recompensa.setDescripcion(recompensaActualizada.getDescripcion());
        recompensa.setImagen(recompensaActualizada.getImagen());
        recompensa.setMetaDonacion(recompensaActualizada.getMetaDonacion());

        return recompensaRepository.save(recompensa);
    }

    @Override
    public void eliminarRecompensa(Long id) {
        recompensaRepository.deleteById(id);
    }
}

