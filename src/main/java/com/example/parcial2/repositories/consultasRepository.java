package com.example.parcial2.repositories;

import com.example.parcial2.entities.consultas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface consultasRepository extends JpaRepository<consultas, Long> {
    List<consultas> findByMedicoId(Long medicoId);

    List<consultas> findByNombrePacienteIgnoreCase(String nombrePaciente);
}
