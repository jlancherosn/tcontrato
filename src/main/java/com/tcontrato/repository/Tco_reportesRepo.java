package com.tcontrato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcontrato.model.Tco_reportes;

@Repository
public interface Tco_reportesRepo extends JpaRepository<Tco_reportes, Integer>{
	
	//Cumple el requerimiento de uso de JPA
	@Query(value = "SELECT cuerpo_reporte FROM TCO_REPORTES WHERE id_reporte_oferta = :idReporteOferta", nativeQuery = true)
	String obtenerCuerpoReporte(@Param("idReporteOferta") Integer idReporteOferta);

}