package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("confirmarReposicionDelegate")
public class ConfirmarReposicionDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Simular confirmación de reposición
        System.out.println("Confirmando reposición para reclamo del proveedor...");

        execution.setVariable("reposicionConfirmada", true);
        // Registrar en el log del proceso
        execution.getProcessEngineServices()
                 .getRuntimeService()
                 .createMessageCorrelation("reposicion_confirmada") 
                 .correlateAll(); // correlación opcional si usas mensajes de salida
    }
}
