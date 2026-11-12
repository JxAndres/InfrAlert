package com.infraalert.infra_alert.controller;

import com.infraalert.infra_alert.model.Evidencia;
import com.infraalert.infra_alert.service.EvidenciaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    public EvidenciaController(EvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @PostMapping("/{reporteId}/evidencia")
    public Evidencia subirEvidencia(
            @PathVariable Long reporteId,
            @RequestParam("archivo") MultipartFile archivo) {

        return evidenciaService.guardarEvidencia(reporteId, archivo);
    }

    @GetMapping("/{reporteId}/evidencia")
    public ResponseEntity<Evidencia> obtenerEvidencia(
            @PathVariable Long reporteId) {

        Optional<Evidencia> evidencia =
                evidenciaService.buscarPorReporte(reporteId);

        return evidencia
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{reporteId}/evidencia/imagen")
    public ResponseEntity<byte[]> obtenerImagen(
            @PathVariable Long reporteId) {

        Optional<Evidencia> evidencia =
                evidenciaService.buscarPorReporte(reporteId);

        if (evidencia.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Evidencia e = evidencia.get();

        return ResponseEntity.ok()
                .header("Content-Type", e.getTipoArchivo())
                .body(e.getDatos());
    }

    @DeleteMapping("/evidencia/{evidenciaId}")
    public void eliminarEvidencia(
            @PathVariable Long evidenciaId) {

        evidenciaService.eliminarEvidencia(evidenciaId);
    }
}