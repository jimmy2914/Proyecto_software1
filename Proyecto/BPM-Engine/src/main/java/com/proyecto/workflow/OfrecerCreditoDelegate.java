package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OfrecerCreditoDelegate implements JavaDelegate {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener la variable desde el proceso
        String numeroFactura = (String) execution.getVariable("factura");

        System.out.println("[OfrecerCreditoDelegate] Número de factura recibido: " + numeroFactura);

        // Buscar la factura en la base de datos
        var facturaOpt = facturaRepository.findByNumero(numeroFactura);

        if (facturaOpt.isPresent()) {
            BigDecimal total = facturaOpt.get().getTotal();
            String tipoCredito;

            if (total.compareTo(new BigDecimal("100000")) < 0) {
                tipoCredito = "Pequeño";
            } else if (total.compareTo(new BigDecimal("300000")) <= 0) {
                tipoCredito = "Mediano";
            } else {
                tipoCredito = "Grande";
            }

            execution.setVariable("tipoCredito", tipoCredito);

        } else {
            String mensaje = "No evaluado - factura no encontrada";
            execution.setVariable("tipoCredito", mensaje);
        }
    }
}