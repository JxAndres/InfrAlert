package com.infraalert.infra_alert.service;

import com.infraalert.infra_alert.model.Funcionario;
import com.infraalert.infra_alert.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario guardarFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void eliminarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }
}