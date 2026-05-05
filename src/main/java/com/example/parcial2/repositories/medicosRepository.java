package com.example.parcial2.repositories;

import com.example.parcial2.entities.medicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface medicosRepository extends JpaRepository<medicos, Long> {
}
