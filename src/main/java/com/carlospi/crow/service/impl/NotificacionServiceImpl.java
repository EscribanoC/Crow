package com.carlospi.crow.service.impl;

import com.carlospi.crow.model.Notificacion;
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
    public Notificacion crearNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @Override
    public Notificacion actualizarNotificacion(Long id, Notificacion notificacionActualizada) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setTipo(notificacionActualizada.getTipo());
        notificacion.setMensaje(notificacionActualizada.getMensaje());
        // Si quieres permitir actualizar la fecha, también puedes incluirla

        return notificacionRepository.save(notificacion);
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

