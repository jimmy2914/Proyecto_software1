    package com.proyecto.workflow.controller;

    import org.springframework.core.io.ClassPathResource;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.nio.charset.StandardCharsets;
    import java.io.IOException;

    @RestController
    @RequestMapping("/api/formularios")
    @CrossOrigin
    public class FormularioController {

        @GetMapping("/{procesoId}")
        public ResponseEntity<String> obtenerFormulario(@PathVariable String procesoId) throws IOException {
            // Mapear ID a archivo (puedes extender esto después)
            if (procesoId.equalsIgnoreCase("proceso_devolucion")) {
                var resource = new ClassPathResource("static/diligenciar_solicitud.form");
                String contenido = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return ResponseEntity.ok(contenido);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }