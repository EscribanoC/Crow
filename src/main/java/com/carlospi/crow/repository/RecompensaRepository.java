package com.carlospi.crow.repository;

import com.carlospi.crow.model.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
}

