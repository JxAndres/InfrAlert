package com.infraalert.infra_alert.repository;

import com.infraalert.infra_alert.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}