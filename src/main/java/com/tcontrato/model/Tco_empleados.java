package com.tcontrato.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "TCO_EMPLEADOS")
public class Tco_empleados {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDEMPLEADO", updatable = false)
	private Integer idEmpleado;
	
	@Column(name = "NOMBRE_EMPLEADO")
	private String nombre_empleado;
	
	@Column(name = "APELLIDO_EMPLEADO")
	private String apellido_empleado;
	
	@JoinColumn(name = "ID_EMPLEADO_ROL", nullable=false)
	@ManyToOne(targetEntity = Tco_roles.class, optional=false, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Tco_roles rol;
	
	@Column(name = "CARGO")
	private String cargo;
		
}