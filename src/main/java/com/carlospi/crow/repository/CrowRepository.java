package com.carlospi.crow.repository;

import com.carlospi.crow.model.Crow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrowRepository extends JpaRepository<Crow, Long> {
}
