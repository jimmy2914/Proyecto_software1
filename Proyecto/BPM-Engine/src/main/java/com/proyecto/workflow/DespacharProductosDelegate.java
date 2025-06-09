package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("despacharProductosDelegate")
public class DespacharProductosDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Obtener el tipo de producto desde las variables del proceso
        String tipoProducto = (String) execution.getVariable("tipoProducto");

        // Imprimir en consola
        System.out.println("Producto (" + tipoProducto + ") despachado correctamente.");

        // Opcionalmente puedes establecer una variable de confirmación
        execution.setVariable("despachoRealizado", true);
    }
}