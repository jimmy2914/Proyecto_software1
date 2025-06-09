package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component("enviarReclamoProveedor")
public class EnviarReclamoProveedorDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Aquí simulas el envío del mensaje a los proveedores
        String producto = (String) execution.getVariable("tipoProducto");
        String motivo = (String) execution.getVariable("motivo");

        // Log para saber qué se está enviando
        System.out.println("[INFO] Enviando reclamo al proveedor:");
        System.out.println("Producto: " + producto);
        System.out.println("Motivo: " + motivo);

        execution.setVariable("mensajeEnviado", true);
    }
}
