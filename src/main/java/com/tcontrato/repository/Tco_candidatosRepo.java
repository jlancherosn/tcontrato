package com.tcontrato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcontrato.model.Tco_candidatos;

@Repository
public interface Tco_candidatosRepo extends JpaRepository<Tco_candidatos, Integer> {
	
	Tco_candidatos findTco_candidatosByIdCandidato(Integer id);
	
}