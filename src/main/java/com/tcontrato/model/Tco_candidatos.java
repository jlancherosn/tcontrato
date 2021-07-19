package com.tcontrato.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TCO_CANDIDATOS"
		, indexes = {@Index(name = "IDX_CANDID_01"
							, columnList = "SEXO, CIUDAD_RESIDENCIA, EDAD")})
public class Tco_candidatos{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDCANDIDATO") // El nombre en mayúscula sostenida para que JPA
								  //no cree otra columna de identificación nueva	
	private Integer idCandidato;
	
	@Column(name = "NOMBRE_CANDIDATO")
	private String nombre_candidato;
	
	@Column(name = "APELLIDO_CANDIDATO")
	private String apellido_candidato;
	
	@Column(name = "TIPO_DOCUMENTO")
	private String tipo_documento;
	
	@Column(name = "NUMERO_DOCUMENTO")
	private String numero_documento;
	
	@Column(name = "CIUDAD_RESIDENCIA")
	private String ciudad_residencia;
	
	@Column(name = "SEXO")
	private String sexo;
	
	@Column(name = "EDAD")
	private String edad;
	
	@Column(name = "TIEMPO_EXPERIENCIA")
	private String tiempo_experiencia;
	
	@ManyToOne(targetEntity = Tco_roles.class, optional=false, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "ID_CANDIDATO_ROL", referencedColumnName = "idRol", nullable = false)    
	private Tco_roles id_candidato_rol;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "TCO_CANDIDATOS_FORMACIONES",
			joinColumns = @JoinColumn(
					name = "ID_CANDIDATO_FORMACION",
					referencedColumnName = "idCandidato"),
			inverseJoinColumns = @JoinColumn(
					name = "ID_FORMACION_CANDIDATO",
					referencedColumnName = "idFormacion")
	)
	private Set<Tco_formaciones> formaciones;
	
	public void addFormacion(Tco_formaciones formacion) {
		if(this.formaciones == null) {
			this.formaciones = new HashSet<>();
		}
		
		this.formaciones.add(formacion);
	}
	
	@JoinTable(
			name = "TCO_CANDIDATOS_OFERTAS",
			joinColumns = @JoinColumn(
					name = "ID_CANDIDATO_OFERTA",
					referencedColumnName = "idCandidato"),
			inverseJoinColumns = @JoinColumn(
					name = "ID_OFERTA_CANDIDATO",
					referencedColumnName = "idOferta")
	)
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Tco_ofertas> ofertas;
	
	public void addFormacion(Tco_ofertas oferta) {
		if(this.ofertas == null) {
			this.ofertas = new ArrayList<>();
		}
		
		this.ofertas.add(oferta);
	}
	
}