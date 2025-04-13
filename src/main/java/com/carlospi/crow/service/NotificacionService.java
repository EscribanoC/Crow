package com.carlospi.crow.service;

import com.carlospi.crow.model.Notificacion;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {
    Notificacion crearNotificacion(Notificacion notificacion);
    List<Notificacion> obtenerPorUsuario(Long usuarioId);
    Notificacion actualizarNotificacion(Long id, Notificacion notificacion);
    Optional<Notificacion> obtenerPorId(Long id);
    void eliminarNotificacion(Long id);
}
