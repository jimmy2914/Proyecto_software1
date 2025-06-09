package com.proyecto.workflow;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    boolean existsByNumero(String numero);
    Optional<Factura> findByNumero(String numero);
}
