package com.tcontrato.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcontrato.model.Tco_formaciones;

@Repository
public interface Tco_formacionesRepo extends JpaRepository<Tco_formaciones, Integer> {
	
	@Query(value = "SELECT formac.nivel_formacion FROM Tco_formaciones formac\r\n"
			+ "INNER JOIN Tco_candidatos_formaciones canfor ON canfor.id_formacion_candidato = formac.idFormacion \r\n"
			+ "INNER JOIN tco_candidatos candid ON canfor.id_candidato_formacion = candid.idcandidato\r\n"
			+ "WHERE candid.idCandidato = ?1", nativeQuery = true)
	Set<?> obtenerNivelesFormacionesCandidato(Integer idCandidato);
	
	@Query(value = "SELECT formac.titulo_formacion FROM Tco_formaciones formac\r\n"
			+ "INNER JOIN Tco_candidatos_formaciones canfor ON canfor.id_formacion_candidato = formac.idFormacion \r\n"
			+ "INNER JOIN tco_candidatos candid ON canfor.id_candidato_formacion = candid.idcandidato\r\n"
			+ "WHERE candid.idCandidato = ?1", nativeQuery = true)
	Set<?> obtenerTitulosFormacionesCandidato(Integer idCandidato);

}