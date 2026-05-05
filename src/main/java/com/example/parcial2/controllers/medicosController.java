package com.example.parcial2.controllers;

import com.example.parcial2.entities.medicos;
import com.example.parcial2.repositories.medicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicos")
public class medicosController {

    @Autowired
    private medicosRepository repo;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaMedicos", repo.findAll());
        return "medicos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("medico", new medicos());
        return "medico-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        medicos medico = repo.findById(id).orElse(null);
        if (medico == null) {
            return "error/403";
        }
        model.addAttribute("medico", medico);
        return "medico-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("medico") medicos medico) {
        repo.save(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/medicos";
    }
}
