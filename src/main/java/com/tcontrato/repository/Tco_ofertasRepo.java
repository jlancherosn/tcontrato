package com.tcontrato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcontrato.model.Tco_ofertas;

@Repository
public interface Tco_ofertasRepo extends JpaRepository<Tco_ofertas, Integer>{

	@Query(value = "SELECT canofe.ponderacion \r\n"
			+ "FROM Tco_candidatos_ofertas canofe \r\n"
			+ "INNER JOIN Tco_candidatos candid ON canofe.id_candidato_oferta = candid.idCandidato \r\n"
			+ "WHERE candid.idCandidato = ?1", nativeQuery = true)
	Integer obtenerPonderacion(Integer idCandidato);
	
}