package com.infraalert.infra_alert.repository;

import com.infraalert.infra_alert.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByFuncionarioIsNull();

    List<Reporte> findByFuncionarioId(Long funcionarioId);

    List<Reporte> findByEstado(String estado);

    List<Reporte> findByCategoriaId(Long categoriaId);

    List<Reporte> findByTituloContainingIgnoreCase(String titulo);

    List<Reporte> findByUbicacionContainingIgnoreCase(String ubicacion);

    long countByEstado(String estado);

    long countByFuncionarioIsNull();

    @Query("""
        SELECT r.categoria.nombre, COUNT(r)
        FROM Reporte r
        WHERE r.categoria IS NOT NULL
        GROUP BY r.categoria.nombre
        ORDER BY COUNT(r) DESC
    """)
    List<Object[]> contarPorCategoria();

    @Query("""
        SELECT CONCAT(f.nombre, ' ', f.apellido), COUNT(r)
        FROM Reporte r
        JOIN r.funcionario f
        GROUP BY f.id, f.nombre, f.apellido
        ORDER BY COUNT(r) DESC
    """)
    List<Object[]> contarPorFuncionario();
}