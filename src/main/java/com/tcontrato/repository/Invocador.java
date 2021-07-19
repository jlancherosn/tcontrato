package com.tcontrato.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

@Repository
public class Invocador {
	
	private EntityManager em;	

	public Invocador(EntityManager em) {
		this.em = em;
	}

	//Cumple el requerimiento de JPQL
	public String listarCriterios() {
		String salida = (String) em.createNativeQuery("SELECT listarCriterios() FROM DUAL")
				.getSingleResult();
		return salida;
	}
	
	//Cumple el requerimiento de JPQL
	public String obtenerNombreTabla(String criterio) {		
		String nombreTabla = (String) em.createNativeQuery("SELECT obtenerNombreTabla(:c1) FROM DUAL")
				.setParameter("c1", criterio)
				.getSingleResult();
		return nombreTabla;
	}
	
	//Cumple el requerimiento de JPQL
	public String obtenerAlias (String nombreTabla) {
		String alias = (String) em.createNativeQuery("SELECT obtenerAlias(:nombreTabla) FROM DUAL")
				.setParameter("nombreTabla", nombreTabla)
				.getSingleResult();
		return alias;
	}
	
	//Cumple el requerimiento de JPQL
	public void guardarReporte(String cuerpoReporte, Integer idReporteOferta, Date fechaCreacion) {
		List<?> query = em.createNativeQuery("SELECT id_reporte_oferta FROM TCO_REPORTES WHERE id_reporte_oferta = :idReporteOferta")
				.setParameter("idReporteOferta", idReporteOferta)
				.getResultList();
		System.out.println(query);
		if(query.size() == 0) {
			em.createNativeQuery("INSERT INTO tco_reportes(cuerpo_reporte, id_reporte_oferta, fecha_creacion) "
					+ "VALUES (:cuerpoReporte, :idReporteOferta, :fechaCreacion)")
			.setParameter("cuerpoReporte", cuerpoReporte)
			.setParameter("idReporteOferta", idReporteOferta)
			.setParameter("fechaCreacion", fechaCreacion)
			.executeUpdate();
		}
		else {
			em.createNativeQuery("UPDATE TCO_REPORTES SET cuerpo_reporte = :cuerpoReporte, fecha_creacion = :fechaCreacion"
					+ " WHERE id_reporte_oferta = :idReporteOferta")
			.setParameter("cuerpoReporte", cuerpoReporte)
			.setParameter("idReporteOferta", idReporteOferta)
			.setParameter("fechaCreacion", fechaCreacion)
			.executeUpdate();
			System.out.println("Cuerpo del reporte actualizado");
		}
	}
	
	//Cumple el requerimiento de JPQL
	public String obtenerCuerpoReporte(Integer idOferta) {
		String cuerpoReporte = (String) em.createNativeQuery("SELECT cuerpo_reporte FROM TCO_REPORTES WHERE id_reporte_oferta = :idOferta")
				.setParameter("idOferta", idOferta)
				.getSingleResult();
		return cuerpoReporte;
	}
	
}