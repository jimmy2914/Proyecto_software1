package com.proyecto.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

@Component("generarCreditoDelegate")
public class GenerarCreditoDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String numeroFactura = (String) execution.getVariable("factura");
        String nombreCliente = (String) execution.getVariable("nombreCliente");

        // Obtener y convertir la variable 'total'
        Object totalObj = execution.getVariable("total");
        BigDecimal monto;
        if (totalObj instanceof BigDecimal) {
            monto = (BigDecimal) totalObj;
        } else if (totalObj instanceof Number) {
            monto = BigDecimal.valueOf(((Number) totalObj).doubleValue());
        } else if (totalObj instanceof String) {
            monto = new BigDecimal((String) totalObj);
        } else {
            throw new IllegalArgumentException("La variable 'total' no es un tipo numérico compatible");
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9004/api/creditos";

        Map<String, Object> body = Map.of(
            "factura", numeroFactura,
            "cliente", nombreCliente,
            "monto", monto
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}
