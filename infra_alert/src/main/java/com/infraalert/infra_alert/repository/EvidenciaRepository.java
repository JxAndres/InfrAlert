package com.infraalert.infra_alert.repository;

import com.infraalert.infra_alert.model.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {

    Optional<Evidencia> findByReporteId(Long reporteId);

}