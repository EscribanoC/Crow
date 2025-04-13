package com.carlospi.crow.service;

import com.carlospi.crow.model.Donacion;

import java.util.List;

public interface DonacionService {
    Donacion crearDonacion(Donacion donacion);
    List<Donacion> listarDonaciones();
    List<Donacion> obtenerPorUsuario(Long usuarioId);
    List<Donacion> obtenerPorCrow(Long crowId);
    Donacion actualizarDonacion(Long id, Donacion donacion);
}
