package com.example.parcial2.controllers;

import com.example.parcial2.entities.consultas;
import com.example.parcial2.repositories.medicosRepository;
import com.example.parcial2.services.consultasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/consultas")
@Tag(name = "Gestion de Consultas", description = "Servicios para registrar consultorios, horarios y asignacion de medicos")
public class consultasController {

    @Autowired
    private consultasService service;

    @Autowired
    private medicosRepository medicoRepo;

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Muestra las consultas segun el rol del usuario autenticado.")
    public String listar(Model model, Principal principal, Authentication auth) {
        List<consultas> consultas;

        if (tieneRol(auth, "ROLE_MEDICO")) {
            consultas = service.listarPorMedico(idMedicoDesdeUsuario(principal.getName()));
        } else if (tieneRol(auth, "ROLE_PACIENTE")) {
            consultas = service.listarPorPaciente(nombrePacienteDesdeUsuario(principal.getName()));
        } else {
            consultas = service.listarTodas();
        }

        model.addAttribute("listaConsultas", consultas);
        return "lista";
    }

    @GetMapping("/nuevo")
    @Operation(summary = "Formulario de creacion", description = "Muestra el formulario para registrar una nueva consulta.")
    public String mostrarFormulario(Model model) {
        model.addAttribute("consulta", new consultas());
        model.addAttribute("listaMedicos", medicoRepo.findAll());
        return "formulario";
    }

    @GetMapping("/editar/{id}")
    @Operation(summary = "Formulario de edicion", description = "Permite editar una consulta segun los permisos del usuario.")
    public String mostrarEditar(@PathVariable("id") Long id, Model model, Principal principal, Authentication auth) {
        consultas consulta = service.buscarPorId(id);

        if (consulta == null || tieneRol(auth, "ROLE_MEDICO") || !puedeEditar(consulta, principal, auth)) {
            return "error/403";
        }

        model.addAttribute("consulta", consulta);
        model.addAttribute("listaMedicos", medicoRepo.findAll());
        return "editar";
    }

    @PostMapping("/guardar")
    @Operation(summary = "Guardar consulta", description = "Procesa el formulario para crear o actualizar una consulta.")
    public String guardar(@ModelAttribute("consulta") consultas consulta, Principal principal, Authentication auth) {
        if (tieneRol(auth, "ROLE_PACIENTE")) {
            consultas consultaActual = service.buscarPorId(consulta.getId());
            if (consultaActual == null || !perteneceAlPaciente(consultaActual, principal.getName())) {
                return "error/403";
            }
            consultaActual.setHoraInicio(consulta.getHoraInicio());
            consultaActual.setHoraFin(consulta.getHoraFin());
            service.guardar(consultaActual);
            return "redirect:/consultas";
        }

        service.guardar(consulta);
        return "redirect:/consultas";
    }

    @GetMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar consulta", description = "Elimina permanentemente una consulta mediante su ID.")
    public String eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
        return "redirect:/consultas";
    }

    private boolean puedeEditar(consultas consulta, Principal principal, Authentication auth) {
        return tieneRol(auth, "ROLE_ADMINISTRADOR") || perteneceAlPaciente(consulta, principal.getName());
    }

    private boolean perteneceAlPaciente(consultas consulta, String username) {
        return consulta.getNombrePaciente() != null
                && consulta.getNombrePaciente().equalsIgnoreCase(nombrePacienteDesdeUsuario(username));
    }

    private boolean tieneRol(Authentication auth, String rol) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(rol));
    }

    private Long idMedicoDesdeUsuario(String username) {
        if ("MED-101".equalsIgnoreCase(username)) {
            return 1L;
        }
        if ("MED-102".equalsIgnoreCase(username)) {
            return 2L;
        }
        if ("MED-103".equalsIgnoreCase(username)) {
            return 3L;
        }
        return -1L;
    }

    private String nombrePacienteDesdeUsuario(String username) {
        if ("PAC-001".equalsIgnoreCase(username)) {
            return "Paciente Uno";
        }
        if ("PAC-002".equalsIgnoreCase(username)) {
            return "Paciente Dos";
        }
        return username;
    }
}
