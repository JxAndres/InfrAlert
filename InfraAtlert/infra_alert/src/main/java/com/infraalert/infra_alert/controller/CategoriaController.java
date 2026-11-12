package com.infraalert.infra_alert.controller;

import com.infraalert.infra_alert.model.Categoria;
import com.infraalert.infra_alert.service.CategoriaService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Long id) {

        return categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoría no encontrada"));
    }

    @PostMapping
    public Categoria guardarCategoria(
            @Valid @RequestBody Categoria categoria) {
        return categoriaService.guardarCategoria(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}