package com.infraalert.infra_alert.service;

import com.infraalert.infra_alert.model.Reporte;
import com.infraalert.infra_alert.model.Funcionario;
import com.infraalert.infra_alert.repository.ReporteRepository;
import com.infraalert.infra_alert.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ReporteService(
            ReporteRepository reporteRepository,
            FuncionarioRepository funcionarioRepository) {

        this.reporteRepository = reporteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    public List<Reporte> listarReportesSinAsignar() {
        return reporteRepository.findByFuncionarioIsNull();
    }

    public Optional<Reporte> buscarPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public Reporte actualizarReporte(Long id, Reporte reporteActualizado) {

        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"));

        reporte.setTitulo(reporteActualizado.getTitulo());
        reporte.setDescripcion(reporteActualizado.getDescripcion());
        reporte.setCategoria(reporteActualizado.getCategoria());
        reporte.setUbicacion(reporteActualizado.getUbicacion());
        reporte.setEstado(reporteActualizado.getEstado());
        reporte.setUsuario(reporteActualizado.getUsuario());
        reporte.setFuncionario(reporteActualizado.getFuncionario());

        return reporteRepository.save(reporte);
    }

    public Reporte asignarFuncionario(Long reporteId, Long funcionarioId) {

        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Funcionario no encontrado"));

        reporte.setFuncionario(funcionario);

        return reporteRepository.save(reporte);
    }

    public Reporte tomarReporte(Long reporteId, Long funcionarioId) {

        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"));

        // Verificar si el reporte ya está asignado
        if (reporte.getFuncionario() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El Reporte " + reporteId + " ya está asignado a un funcionario");
        }

        // Verificar si el funcionario ya tiene otro reporte
        List<Reporte> reportesFuncionario = reporteRepository.findByFuncionarioId(funcionarioId);

        if (!reportesFuncionario.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya tienes asignado un reporte. Debes terminarlo antes de tomar otro.");
        }

        // Buscar al funcionario
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Funcionario no encontrado"));

        // Asignar el reporte al funcionario
        reporte.setFuncionario(funcionario);

        // Cambiar automáticamente el estado
        reporte.setEstado("EN_PROCESO");

        return reporteRepository.save(reporte);
    }

    public Reporte cambiarEstado(Long reporteId, String nuevoEstado) {

        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reporte no encontrado"));

        String estadoActual = reporte.getEstado();

        // Validar que el nuevo estado exista
        if (!nuevoEstado.equals("PENDIENTE")
                && !nuevoEstado.equals("EN_PROCESO")
                && !nuevoEstado.equals("RESUELTO")
                && !nuevoEstado.equals("CANCELADO")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado no válido");
        }

        // PENDIENTE → EN_PROCESO o CANCELADO
        if (estadoActual.equals("PENDIENTE")) {

            if (!nuevoEstado.equals("EN_PROCESO")
                    && !nuevoEstado.equals("CANCELADO")) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "No se puede cambiar de PENDIENTE a " + nuevoEstado);
            }
        }

        // EN_PROCESO → RESUELTO o CANCELADO
        if (estadoActual.equals("EN_PROCESO")) {

            if (!nuevoEstado.equals("RESUELTO")
                    && !nuevoEstado.equals("CANCELADO")) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "No se puede cambiar de EN_PROCESO a " + nuevoEstado);
            }
        }

        // RESUELTO y CANCELADO son estados finales
        if (estadoActual.equals("RESUELTO")
                || estadoActual.equals("CANCELADO")) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El reporte ya se encuentra en un estado final");
        }

        reporte.setEstado(nuevoEstado);

        return reporteRepository.save(reporte);
    }

    public List<Reporte> listarPorEstado(String estado) {
        return reporteRepository.findByEstado(estado);
    }

    public List<Reporte> listarPorCategoria(Long categoriaId) {
        return reporteRepository.findByCategoriaId(categoriaId);
    }

    public List<Reporte> listarPorFuncionario(Long funcionarioId) {
        return reporteRepository.findByFuncionarioId(funcionarioId);
    }

    public List<Reporte> buscarPorTitulo(String titulo) {
        return reporteRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Reporte> buscarPorUbicacion(String ubicacion) {
        return reporteRepository.findByUbicacionContainingIgnoreCase(ubicacion);
    }

    public java.util.Map<String, Object> obtenerEstadisticas() {

        java.util.Map<String, Object> estadisticas = new java.util.HashMap<>();

        estadisticas.put("total", reporteRepository.count());
        estadisticas.put("pendientes", reporteRepository.countByEstado("PENDIENTE"));
        estadisticas.put("enProceso", reporteRepository.countByEstado("EN_PROCESO"));
        estadisticas.put("resueltos", reporteRepository.countByEstado("RESUELTO"));
        estadisticas.put("cancelados", reporteRepository.countByEstado("CANCELADO"));
        estadisticas.put("sinAsignar", reporteRepository.countByFuncionarioIsNull());

        java.util.List<java.util.Map<String, Object>> porCategoria = new java.util.ArrayList<>();

        for (Object[] resultado : reporteRepository.contarPorCategoria()) {

            java.util.Map<String, Object> categoria = new java.util.HashMap<>();

            categoria.put("categoria", resultado[0]);
            categoria.put("cantidad", resultado[1]);

            porCategoria.add(categoria);
        }

        estadisticas.put("porCategoria", porCategoria);

        java.util.List<java.util.Map<String, Object>> porFuncionario = new java.util.ArrayList<>();

        for (Object[] resultado : reporteRepository.contarPorFuncionario()) {

            java.util.Map<String, Object> funcionario = new java.util.HashMap<>();

            funcionario.put("funcionario", resultado[0]);
            funcionario.put("cantidad", resultado[1]);

            porFuncionario.add(funcionario);
        }

        estadisticas.put("porFuncionario", porFuncionario);

        return estadisticas;
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}