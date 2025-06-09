package com.proyecto.workflow;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class MiServiceTaskDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String tipo = (String) execution.getVariable("tipoProducto");
        System.out.println("Iniciando petición a proveedores por productos tipo: " + tipo);
        
    }
}
