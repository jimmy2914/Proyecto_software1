package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("verificarFacturaDelegate")
public class VerificarFacturaDelegate implements JavaDelegate {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String numeroFactura = (String) execution.getVariable("factura");

        Optional<Factura> optionalFactura = facturaRepository.findByNumero(numeroFactura);

        if (optionalFactura.isPresent()) {
            Factura factura = optionalFactura.get();

            execution.setVariable("facturaValidacion", "Valida");

            // Guardamos campos individuales como variables simples
            execution.setVariable("nombreCliente", factura.getNombreCliente());
            execution.setVariable("total", factura.getTotal());

            System.out.println("Factura encontrada: " + factura.getNumero() +
                " | Cliente: " + factura.getNombreCliente() +
                " | Total: " + factura.getTotal());

        } else {
            execution.setVariable("facturaValidacion", "Sin factura");
            System.out.println("Factura no encontrada: " + numeroFactura);
        }
    }
}
