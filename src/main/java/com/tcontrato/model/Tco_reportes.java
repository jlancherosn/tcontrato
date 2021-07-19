package com.tcontrato.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data

@Entity
@Table(name = "TCO_REPORTES"
		, indexes = @Index(name = "IDX_REPORT_01"
							, columnList = "FECHA_CREACION"))
public class Tco_reportes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDREPORTE", updatable = false)
	private Integer idReporte;
	
	@Column(name = "CUERPO_REPORTE")
	private String cuerpo_reporte;
	
	@JoinColumn(name = "ID_REPORTE_OFERTA", nullable=false)
	@ManyToOne(optional=false, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Tco_ofertas id_reporte_oferta;
	
	@Column(name = "FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fecha_creacion;

}