package com.proyecto.workflow.controller;

import com.proyecto.workflow.model.ProcesoDTO;
import com.proyecto.workflow.service.ProcesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procesos")
@CrossOrigin
public class ProcesoController {

    @Autowired
    private ProcesoService procesoService;

    @GetMapping
    public List<ProcesoDTO> listarProcesos() {
        return procesoService.obtenerProcesosDisponibles();
    }
}