package com.proyecto.treasury;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OperacionesTesoreriaController {

    @PostMapping("/creditos")
    public ResponseEntity<String> generarCredito(@RequestBody Map<String, Object> datos) {
        System.out.println("Crédito generado: " + datos);
        return ResponseEntity.ok("Crédito procesado");
    }

    @PostMapping("/reembolsos")
    public ResponseEntity<String> generarReembolso(@RequestBody Map<String, Object> datos) {
        System.out.println("Reembolso generado: " + datos);
        return ResponseEntity.ok("Reembolso enviado");
    }
}
