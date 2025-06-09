package com.proyecto.workflow.service;

import com.proyecto.workflow.model.ProcesoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcesoService {

    public List<ProcesoDTO> obtenerProcesosDisponibles() {
        return List.of(
            new ProcesoDTO("Proceso_devolucion", "Proceso de Devolución")
        );
    }
}