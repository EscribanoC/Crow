package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Donacion;
import com.carlospi.crow.repository.DonacionRepository;
import com.carlospi.crow.service.DonacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;

    public DonacionServiceImpl(DonacionRepository donacionRepository) {
        this.donacionRepository = donacionRepository;
    }

    @Override
    public Donacion crearDonacion(Donacion donacion) {
        return donacionRepository.save(donacion);
    }

    @Override
    public List<Donacion> listarDonaciones() {
        return donacionRepository.findAll();
    }

    @Override
    public List<Donacion> obtenerPorUsuario(Long usuarioId) {
        return donacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Donacion> obtenerPorCrow(Long crowId) {
        return donacionRepository.findByCrowId(crowId);
    }

    @Override
    public Donacion actualizarDonacion(Long id, Donacion donacionActualizada) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donación no encontrada"));

        donacion.setCantidad(donacionActualizada.getCantidad());
        donacion.setUsuario(donacionActualizada.getUsuario());
        donacion.setCrow(donacionActualizada.getCrow());

        return donacionRepository.save(donacion);
    }

    @Override
    public List<Donacion> obtenerDonacionesPorUsuario(Long id) {return donacionRepository.findByUsuarioId(id);}

    @Override
    public List<Donacion> obtenerDonacionesPorCrow(Long crowId) {return donacionRepository.findByCrowId(crowId);}


}
