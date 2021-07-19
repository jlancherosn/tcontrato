package com.tcontrato.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "TCO_ROLES")
public class Tco_roles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDROL", updatable = false)
	private Integer idRol;
	
	@Column(name = "NOMBRE_ROL")
	private String nombre_rol;

}