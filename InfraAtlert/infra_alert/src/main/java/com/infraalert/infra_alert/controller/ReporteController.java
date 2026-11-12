package com.infraalert.infra_alert.controller;

import com.infraalert.infra_alert.model.Reporte;
import com.infraalert.infra_alert.service.ReporteService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }

    @GetMapping("/estadisticas")
    public java.util.Map<String, Object> obtenerEstadisticas() {
        return reporteService.obtenerEstadisticas();
    }

    @GetMapping("/sin-asignar")
    public List<Reporte> listarReportesSinAsignar() {
        return reporteService.listarReportesSinAsignar();
    }

    @GetMapping("/estado/{estado}")
    public List<Reporte> listarPorEstado(
            @PathVariable String estado) {

        return reporteService.listarPorEstado(estado);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Reporte> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return reporteService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public List<Reporte> listarPorFuncionario(
            @PathVariable Long funcionarioId) {

        return reporteService.listarPorFuncionario(funcionarioId);
    }

    @GetMapping("/buscar/titulo/{titulo}")
    public List<Reporte> buscarPorTitulo(
            @PathVariable String titulo) {

        return reporteService.buscarPorTitulo(titulo);
    }

    @GetMapping("/buscar/ubicacion/{ubicacion}")
    public List<Reporte> buscarPorUbicacion(
            @PathVariable String ubicacion) {

        return reporteService.buscarPorUbicacion(ubicacion);
    }

    @GetMapping("/{id}")
    public Reporte buscarPorId(@PathVariable Long id) {
        return reporteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"));
    }

    @PostMapping
    public Reporte guardarReporte(@Valid @RequestBody Reporte reporte) {
        return reporteService.guardarReporte(reporte);
    }

    @PutMapping("/{id}")
    public Reporte actualizarReporte(
            @PathVariable Long id,
            @Valid @RequestBody Reporte reporte) {

        return reporteService.actualizarReporte(id, reporte);
    }

    @PutMapping("/{reporteId}/funcionario/{funcionarioId}")
    public Reporte asignarFuncionario(
            @PathVariable Long reporteId,
            @PathVariable Long funcionarioId) {

        return reporteService.asignarFuncionario(reporteId, funcionarioId);
    }

    @PutMapping("/{reporteId}/tomar/{funcionarioId}")
    public Reporte tomarReporte(
            @PathVariable Long reporteId,
            @PathVariable Long funcionarioId) {

        return reporteService.tomarReporte(reporteId, funcionarioId);
    }

    @PutMapping("/{reporteId}/estado/{nuevoEstado}")
    public Reporte cambiarEstado(
            @PathVariable Long reporteId,
            @PathVariable String nuevoEstado) {

        return reporteService.cambiarEstado(reporteId, nuevoEstado);
    }

    @DeleteMapping("/{id}")
    public void eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
    }
}