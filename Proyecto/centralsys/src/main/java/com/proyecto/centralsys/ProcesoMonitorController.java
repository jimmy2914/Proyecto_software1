package com.proyecto.centralsys;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin
public class ProcesoMonitorController {

    private final String BPM_ENGINE_URL = "http://localhost:9000/engine-rest";

    @GetMapping("/instancias-devolucion")
    public ResponseEntity<?> obtenerHistorialDevoluciones() {
        try {
            RestTemplate rt = new RestTemplate();

            // Obtener todas las instancias históricas del proceso
            var url = BPM_ENGINE_URL + "/history/process-instance?processDefinitionKey=Proceso_devolucion&sortBy=startTime&sortOrder=desc";
            var instancias = rt.getForObject(url, List.class);

            return ResponseEntity.ok(instancias);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar historial: " + e.getMessage());
        }
    }

    @GetMapping("/variables/{processInstanceId}")
    public ResponseEntity<?> obtenerVariablesDeInstancia(@PathVariable String processInstanceId) {
        try {
            RestTemplate rt = new RestTemplate();

            var url = BPM_ENGINE_URL + "/history/variable-instance?processInstanceId=" + processInstanceId;
            var variables = rt.getForObject(url, List.class);

            return ResponseEntity.ok(variables);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener variables: " + e.getMessage());
        }
    }

    @GetMapping("/tareas/{processInstanceId}")
    public ResponseEntity<?> obtenerTareasDeInstancia(@PathVariable String processInstanceId) {
        try {
            RestTemplate rt = new RestTemplate();

            // Endpoint Camunda para obtener tareas activas por instancia
            String url = BPM_ENGINE_URL + "/task?processInstanceId=" + processInstanceId;
            List<?> tareas = rt.getForObject(url, List.class);

            return ResponseEntity.ok(tareas);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener tareas: " + e.getMessage());
    }
}

@PostMapping("/tarea/finalizar/{taskId}")
public ResponseEntity<?> finalizarTarea(@PathVariable String taskId) {
    try {
        RestTemplate rt = new RestTemplate();

        // 1. Claim (por si acaso)
        String claimUrl = BPM_ENGINE_URL + "/task/" + taskId + "/claim";
        rt.postForEntity(claimUrl, Map.of("userId", "demo"), String.class);

        // 2. Complete
        String completeUrl = BPM_ENGINE_URL + "/task/" + taskId + "/complete";
        rt.postForEntity(completeUrl, Map.of(), String.class);

        return ResponseEntity.ok("Tarea completada");

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al finalizar tarea: " + e.getMessage());
    }
}

}
