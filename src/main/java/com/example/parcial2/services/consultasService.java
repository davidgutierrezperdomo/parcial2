package com.example.parcial2.services;

import com.example.parcial2.entities.consultas;
import com.example.parcial2.repositories.consultasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class consultasService {

    @Autowired
    private consultasRepository repo;

    public List<consultas> listarTodas() {
        return repo.findAll();
    }

    public List<consultas> listarPorMedico(Long medicoId) {
        return repo.findByMedicoId(medicoId);
    }

    public List<consultas> listarPorPaciente(String nombrePaciente) {
        return repo.findByNombrePacienteIgnoreCase(nombrePaciente);
    }

    public void guardar(consultas consulta) {
        repo.save(consulta);
    }

    public consultas buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
