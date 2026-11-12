package com.infraalert.infra_alert.controller;

import com.infraalert.infra_alert.model.Funcionario;
import com.infraalert.infra_alert.service.FuncionarioService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public List<Funcionario> listarFuncionarios() {
        return funcionarioService.listarFuncionarios();
    }

    @GetMapping("/{id}")
    public Funcionario buscarPorId(@PathVariable Long id) {

        return funcionarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Funcionario no encontrado"));
    }

    @PostMapping
    public Funcionario guardarFuncionario(@Valid @RequestBody Funcionario funcionario) {
        return funcionarioService.guardarFuncionario(funcionario);
    }

    @DeleteMapping("/{id}")
    public void eliminarFuncionario(@PathVariable Long id) {
        funcionarioService.eliminarFuncionario(id);
    }
}