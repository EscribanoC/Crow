package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Crow;
import com.carlospi.crow.model.Notificacion;
import com.carlospi.crow.model.Usuario;
import com.carlospi.crow.model.enumeration.TipoNotificacionEnum;
import com.carlospi.crow.repository.NotificacionRepository;
import com.carlospi.crow.service.NotificacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void crearNotificacion(TipoNotificacionEnum tipoNotificacion, Usuario receptor, Usuario emisor, Crow crow) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipoNotificacion);
        notificacion.setReceptor(receptor);
        notificacion.setEmisor(emisor);
        if (crow != null) {
            notificacion.setCrow(crow);
        }
        notificacionRepository.save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByReceptorIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Override
    public Notificacion obtenerNotificacionPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    @Override
    public Optional<Notificacion> obtenerPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    @Override
    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}

