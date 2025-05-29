package com.carlospi.crow.service;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Notificacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.TipoNotificacionEnum;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {
    void crearNotificacion(TipoNotificacionEnum tipoNotificacion, Usuario receptor, Usuario emisor, Crow crow);
    Optional<Notificacion> obtenerPorId(Long id);
    void eliminarNotificacion(Long id);
    List<Notificacion> obtenerNotificacionesPorUsuario(Long id);
    void marcarComoLeida(Long id);
}
