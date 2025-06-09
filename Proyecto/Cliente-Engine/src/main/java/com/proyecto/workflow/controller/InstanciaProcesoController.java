package com.proyecto.workflow.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/instancias")
@CrossOrigin
public class InstanciaProcesoController {

    // Dirección del BPM Engine (ajusta si está en otro host o puerto)
    private final String CAMUNDA_ENGINE_URL = "http://localhost:9000/engine-rest";


    @PostMapping("/iniciar-completo/{procesoId}")
public ResponseEntity<String> iniciarYCompletarPrimeraTarea(@PathVariable String procesoId,
                                                            @RequestBody Map<String, Object> datosFormulario) {
    try {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Preparar variables
        Map<String, Object> variables = new HashMap<>();
        for (Map.Entry<String, Object> entry : datosFormulario.entrySet()) {
            variables.put(entry.getKey(), Map.of(
                "value", entry.getValue(),
                "type", "String"
            ));
        }

        Map<String, Object> body = Map.of("variables", variables);

        // 2. Iniciar instancia
        String startUrl = CAMUNDA_ENGINE_URL + "/process-definition/key/" + procesoId + "/start";
        ResponseEntity<Map> respuesta = restTemplate.postForEntity(startUrl, body, Map.class);
        String instanciaId = respuesta.getBody().get("id").toString();

        // 3. Obtener la tarea pendiente de esa instancia
        String tareasUrl = CAMUNDA_ENGINE_URL + "/task?processInstanceId=" + instanciaId;
        ResponseEntity<Map[]> tareasResponse = restTemplate.getForEntity(tareasUrl, Map[].class);
        if (tareasResponse.getBody().length == 0) {
            return ResponseEntity.status(500).body("No se encontró ninguna tarea tras iniciar el proceso.");
        }
        String taskId = tareasResponse.getBody()[0].get("id").toString();

        // 4. Claim
        String claimUrl = CAMUNDA_ENGINE_URL + "/task/" + taskId + "/claim";
        restTemplate.postForEntity(claimUrl, Map.of("userId", "demo"), String.class);

        // 5. Submit form
        String submitUrl = CAMUNDA_ENGINE_URL + "/task/" + taskId + "/submit-form";
        restTemplate.postForEntity(submitUrl, Map.of("variables", variables), String.class);

        return ResponseEntity.ok("Instancia iniciada y tarea completada correctamente.");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}


    @GetMapping("/probar-conexion")
public ResponseEntity<String> probarConexion() {
    String url = "http://localhost:9000/engine-rest/process-definition";
    try {
        RestTemplate rt = new RestTemplate();
        String resultado = rt.getForObject(url, String.class);
        return ResponseEntity.ok("Conexión exitosa: " + resultado);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error de conexión: " + e.getMessage());
    }
}

@GetMapping("/tarea-actual/{procesoId}")
public ResponseEntity<?> obtenerTareaActual(@PathVariable String procesoId) {
    try {
        RestTemplate restTemplate = new RestTemplate();
        // Buscar la última instancia del proceso
        String instanciaUrl = CAMUNDA_ENGINE_URL + "/process-instance?processDefinitionKey=" + procesoId + "&sortBy=instanceId&sortOrder=desc";
        var instancias = restTemplate.getForObject(instanciaUrl, Map[].class);

        if (instancias.length == 0) {
            return ResponseEntity.status(404).body("No hay instancias activas");
        }

        String instanciaId = instancias[0].get("id").toString();

        // Buscar tareas de esa instancia
        String tareasUrl = CAMUNDA_ENGINE_URL + "/task?processInstanceId=" + instanciaId;
        var tareas = restTemplate.getForObject(tareasUrl, Map[].class);

        if (tareas.length == 0) {
            return ResponseEntity.status(404).body("No hay tareas pendientes");
        }

        String taskId = tareas[0].get("id").toString();
        String taskName = tareas[0].get("name").toString();

        // Obtener variables de la tarea
        String varsUrl = CAMUNDA_ENGINE_URL + "/task/" + taskId + "/form-variables";
        var variables = restTemplate.getForObject(varsUrl, Map.class);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("taskId", taskId);
        respuesta.put("nombreTarea", taskName);
        respuesta.put("variables", variables);

        return ResponseEntity.ok(respuesta);

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al consultar tarea actual: " + e.getMessage());
    }
}

@PostMapping("/tarea/completar/{taskId}")
public ResponseEntity<String> completarTarea(@PathVariable String taskId, @RequestBody Map<String, Object> datosFormulario) {
    try {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Claim
        String claimUrl = CAMUNDA_ENGINE_URL + "/task/" + taskId + "/claim";
        Map<String, Object> claimBody = Map.of("userId", "demo");
        restTemplate.postForObject(claimUrl, claimBody, String.class);

        // 2. Submit
        Map<String, Object> variables = new HashMap<>();
        for (Map.Entry<String, Object> entry : datosFormulario.entrySet()) {
            variables.put(entry.getKey(), Map.of(
                "value", entry.getValue(),
                "type", "String"
            ));
        }

        Map<String, Object> submitBody = Map.of("variables", variables);
        String submitUrl = CAMUNDA_ENGINE_URL + "/task/" + taskId + "/submit-form";

        restTemplate.postForObject(submitUrl, submitBody, String.class);

        return ResponseEntity.ok("Tarea completada correctamente");

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al completar tarea: " + e.getMessage());
    }
}

@PostMapping("/simular-timer")
public ResponseEntity<String> simularTimers() {
    try {
        RestTemplate restTemplate = new RestTemplate();

        String jobsUrl = CAMUNDA_ENGINE_URL + "/job";
        ResponseEntity<Map[]> jobsResponse = restTemplate.getForEntity(jobsUrl, Map[].class);
        Map[] jobs = jobsResponse.getBody();

        int count = 0;
        for (Map job : jobs) {
            String jobId = (String) job.get("id");
            String executeUrl = CAMUNDA_ENGINE_URL + "/job/" + jobId + "/execute";

            // Intentamos ejecutar cada job (puede fallar si no está listo)
            try {
                restTemplate.postForLocation(executeUrl, null);
                count++;
            } catch (Exception e) {
                System.out.println("Error al ejecutar job " + jobId + ": " + e.getMessage());
            }
        }

        return ResponseEntity.ok(count + " job(s) ejecutado(s) manualmente.");

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al ejecutar timers: " + e.getMessage());
    }
}



}
