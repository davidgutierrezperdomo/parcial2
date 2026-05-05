package com.example.parcial2.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class consultas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String nombrePaciente;

    @Column(length = 100)
    private String motivoConsulta;

    private Integer numeroConsultorio;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private medicos medico;
}
