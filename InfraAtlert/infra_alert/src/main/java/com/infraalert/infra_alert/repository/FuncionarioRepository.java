package com.infraalert.infra_alert.repository;

import com.infraalert.infra_alert.model.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

}