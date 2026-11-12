package com.infraalert.infra_alert.repository;

import com.infraalert.infra_alert.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}