package com.carlospi.crow.service;

import com.carlospi.crow.model.Notificacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.TipoNotificacion;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {
    void crearNotificacion(TipoNotificacion tipoNotificacion, String mensaje, Usuario usuario);
    Optional<Notificacion> obtenerPorId(Long id);
    void eliminarNotificacion(Long id);
    List<Notificacion> obtenerNotificacionesPorUsuario(Long id);

    Notificacion obtenerNotificacionPorId(Long id);

}
