package com.tcontrato.service;

import com.tcontrato.dto.Tco_ofertasDto;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

public interface Tco_segmentacionService {
	
	List<Tco_ofertasDto> mostrarOfertas();
	
	/**
	 * Muestra la lista completa de los criterios que puede selleccionar el usuario para hacer la segmentación
	 * de los candidatos
	 * @return la lista completa de los posibles criterios
	 */
	ArrayList<ArrayList<String>> listarCriterios(); //Implementado
	
	/**
	 * Persiste el nombre de la tabla a la que pertenece el criterio (columna)
	 * seleccionado para hacer la segmentación de los candidatos
	 * @param criterio
	 * @return nombre de la tabla
	 */
	String nombreTabla(String criterio); //Implementado
	
	/**
	 * Persiste el alias de la tabla a la que pertenece el criterio (columna)
	 * seleccionado para hacer la segmentaciión de los candidatos
	 * @param criterio
	 * @return alias de la tabla
	 */
	String aliasTabla(String criterio); //Implementado
	
	
	/**
	 * Crea el string que usa el procedimiento masivo, el string se emplea para ejecutar un SQL dinámico para
	 * calcular y asignar el puntaje de ponderación de los candidatos que se postularon a una oferta laboral,
	 * el cálculo se hace según los parámetros que escoge el usuario
	 * @param criteriosSeleccionados
	 * @param nombresTablas
	 * @param ponderaciones
	 * @param prioridades
	 * @param id_oferta_candidato
	 * @return string con la instrucción SQL
	 */
	String crearQueryPonderacion(List<String> criteriosSeleccionados, //Implementado
			List<String> nombresTablas,
			List<Integer> ponderaciones,
			List<List<List<Object>>> prioridades,
			Integer id_oferta_candidato);
	 /**
	  * Crea el string que determina la segmentación a elaborar según los parámetros elegidos por el usuario
	  * @param criteriosSeleccionados
	  * @param ponderaciones
	  * @param prioridades
	  * @param id_oferta_candidato
	  * @return string del conjunto de instrucciones PLSQL que se ejecutan para llevar a cabo el proceso de
	  * segmentación
	  */
	String crearSentenciaPonderacion(List<String> criteriosSeleccionados, //Implementado
			List<Integer> ponderaciones,
			List<List<List<Object>>> prioridades,
			Integer id_oferta_candidato);
	
	/**
	 * Calcula las ponderaciones de los candidatos que se presentan a determinada oferta, el cálculo de las 
	 * ponderaciones se hace con base en los parámetros de segmentación elegidos por el usuario
	 * @param queryStringEntrada
	 * @param listaCriteriosElegidos
	 * @param querySentenciaponderacion
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	void calcularPonderacion(String queryStringEntrada, //Implementado
			List<String> listaCriteriosElegidos,
			String querySentenciaponderacion) throws ClassNotFoundException, SQLException;
		
	/**
	 * Genera la sentencia SQL que muestra la segmentación generada para la oferta laboral especificada
	 * @param idOferta
	 * @param criteriosSeleccionados
	 * @param prioridades
	 * @return
	 */
	String crearSentenciaReporte(Integer idOferta, //Implementado
			List<String> criteriosSeleccionados,
			List<List<List<Object>>> prioridades);
	
	/**
	 * Genera el string necesario para hacer el query de la segmentación de los candidatos
	 * @param stringReporte
	 * @return String para ejecutar el query de la segmentacion
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	String generarSegmentacion(String stringReporte) throws ClassNotFoundException, SQLException; //Implementado
	
	/**
	 * Almacena el string conformado por el conjunto de ids de los candidatos ordenado según la segmentación realizada a partir de
	 * los parámetros establecidos por el usuario
	 * @param cuerpoReporte
	 * @param idReporteOferta
	 * @param fecha
	 * @throws ParseException
	 */
	void guardarReporte(String cuerpoReporte, Integer idReporteOferta, Date fecha) throws ParseException; //Implementado
	
	Object obtenerCandidatoPorId(Integer id);
	
	/**
	 * Genera la lista de los candidatos con sus respectivos datos, la lista está ordenada según la segmentación realizada
	 * @param segmentacion
	 * @return lista de la segmentación de los candidatos
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
//	Collection<Tco_candidatosDto> generarReporte(Integer idOferta); //Implementado
	
	JSONArray generarReporte(Integer idOferta);
	
	/**
	 * Muestra la lista completa de los candidatos que se han registrado a cualquiera de las ofertas
	 * @return lista de todos los candidatos que se han postulado a alguna oferta
	 */
	//Set<Tco_candidatosDto> getAllCandidatos(); //Implementado	
	
//	@Query(nativeQuery = true, value = "SELECT listarCriterios() FROM DUAL")
//	public String listarCriterios();
	
}