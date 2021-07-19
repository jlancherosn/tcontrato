package com.tcontrato.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "TCO_OFERTAS"
		, indexes = @Index(name = "IDX_OFERT_01"
							, columnList = "FECHA_PUBLICACION, TITULO_OFERTA"))
public class Tco_ofertas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDOFERTA", updatable = false)
	private Integer idOferta;
	
	@Column(name = "TITULO_OFERTA")
	private String titulo_oferta;
	
	@Column(name = "ENUNCIADO")
	private String enunciado;
	
	@Column(name = "FECHA_PUBLICACION")
	private String fecha_publicacion;
	
	@JoinColumn(name = "ID_OFERTA_EMPLEADO", nullable = false)
	@ManyToOne(optional=false, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Tco_empleados empleado;
	
	@ManyToMany(mappedBy = "ofertas") // Se mapea al nombre de la lista tipo Tco_ofertas definida en Tco_candidatos
	private List<Tco_candidatos> candidatos;
	
}