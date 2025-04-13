package com.carlospi.crow.repository;

import com.carlospi.crow.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    List<Donacion> findByUsuarioId(Long usuarioId);
    List<Donacion> findByCrowId(Long crowId);
}
