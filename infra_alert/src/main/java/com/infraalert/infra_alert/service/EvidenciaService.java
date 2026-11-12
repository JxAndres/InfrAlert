package com.infraalert.infra_alert.service;

import com.infraalert.infra_alert.model.Evidencia;
import com.infraalert.infra_alert.model.Reporte;
import com.infraalert.infra_alert.repository.EvidenciaRepository;
import com.infraalert.infra_alert.repository.ReporteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Service
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final ReporteRepository reporteRepository;

    public EvidenciaService(
            EvidenciaRepository evidenciaRepository,
            ReporteRepository reporteRepository) {

        this.evidenciaRepository = evidenciaRepository;
        this.reporteRepository = reporteRepository;
    }

    public Evidencia guardarEvidencia(Long reporteId, MultipartFile archivo) {

        // Buscar el reporte
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"
                ));

        // Verificar que se haya enviado un archivo
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar una imagen"
            );
        }

        // Validar tamaño máximo: 2 MB
        if (archivo.getSize() > 2 * 1024 * 1024) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen no puede superar los 2 MB"
            );
        }

        // Validar tipo de archivo
        String tipo = archivo.getContentType();

        if (!"image/jpeg".equals(tipo)
                && !"image/png".equals(tipo)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se permiten imágenes JPG, JPEG o PNG"
            );
        }

        // Verificar que el reporte no tenga ya una evidencia
        Optional<Evidencia> evidenciaExistente =
                evidenciaRepository.findByReporteId(reporteId);

        if (evidenciaExistente.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este reporte ya tiene una evidencia"
            );
        }

        try {

            Evidencia evidencia = new Evidencia();

            evidencia.setNombreArchivo(archivo.getOriginalFilename());
            evidencia.setTipoArchivo(tipo);
            evidencia.setDatos(archivo.getBytes());
            evidencia.setReporte(reporte);

            return evidenciaRepository.save(evidencia);

        } catch (IOException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar la imagen"
            );
        }
    }

    public Optional<Evidencia> buscarPorReporte(Long reporteId) {
        return evidenciaRepository.findByReporteId(reporteId);
    }

    public void eliminarEvidencia(Long evidenciaId) {
        evidenciaRepository.deleteById(evidenciaId);
    }
}