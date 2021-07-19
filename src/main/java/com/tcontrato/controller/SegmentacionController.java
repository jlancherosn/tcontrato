package com.tcontrato.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.tcontrato.dto.Tco_ofertasDto;
import com.tcontrato.dto.Tco_reporteDeCandidatosDtos;
import com.tcontrato.model.Tco_reportes;
import com.tcontrato.service.Tco_segmentacionService;

@RestController
@RequestMapping
public class SegmentacionController {
	
	@Autowired
	private final Tco_segmentacionService tco_segmentacionService;
	
	private List<String> criteriosSeleccionados;
	private List<String> nombresTablas;
	private List<Integer> ponderaciones;
	private List<List<List<Object>>> prioridades;	
	private Integer idOferta;
	private Integer cantidadMostrar;
	
	public SegmentacionController() {
		this.tco_segmentacionService = null;		
	}
	
	public SegmentacionController(Tco_segmentacionService tco_segmentacionService,
			List<String> criteriosSeleccionados, 
			List<String> nombresTablas, 
			List<Integer> ponderaciones,
			List<List<List<Object>>> prioridades) {
		this.tco_segmentacionService = tco_segmentacionService;
		this.criteriosSeleccionados = criteriosSeleccionados;
		this.nombresTablas = nombresTablas;
		this.ponderaciones = ponderaciones;
		this.prioridades = prioridades;
	}
	
	public SegmentacionController(Integer idOferta) {
		this.tco_segmentacionService = null;
		this.idOferta = idOferta;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	// Comienzo declaración de métodos auxiliares
	////////////////////////////////////////////////////////////////////////////////
	
	//Método para convertir a JSONFormat un JSONObject que hayamos recibido desde el servicio (la clase Tco_segmentacionServicio)
	//https://stackoverflow.com/questions/20534862/how-to-efficiently-map-a-org-json-jsonobject-to-a-pojo
	@SuppressWarnings("deprecation")
	static JsonNode convertJsonFormat(JSONObject json) {
	    ObjectNode ret = JsonNodeFactory.instance.objectNode();

	    Iterator<String> iterator = json.keys();
	    for (; iterator.hasNext();) {
	        String key = iterator.next();
	        Object value;
	        try {
	            value = json.get(key);
	        } catch (JSONException e) {
	            throw new RuntimeException(e);
	        }
	        if (json.isNull(key))
	            ret.putNull(key);
	        else if (value instanceof String)
	            ret.put(key, (String) value);
	        else if (value instanceof Integer)
	            ret.put(key, (Integer) value);
	        else if (value instanceof Long)
	            ret.put(key, (Long) value);
	        else if (value instanceof Double)
	            ret.put(key, (Double) value);
	        else if (value instanceof Boolean)
	            ret.put(key, (Boolean) value);
	        else if (value instanceof JSONObject)
	            ret.put(key, convertJsonFormat((JSONObject) value));
	        else if (value instanceof JSONArray)
	            ret.put(key, convertJsonFormat((JSONArray) value));
	        else
	            throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
	    }
	    return ret;
	}
	
	//Método para convertir a JSONFormat un JSONArray que hayamos recibido desde el servicio (la clase Tco_segmentacionServicio)
	//https://stackoverflow.com/questions/20534862/how-to-efficiently-map-a-org-json-jsonobject-to-a-pojo
	static JsonNode convertJsonFormat(JSONArray json) {
	    ArrayNode ret = JsonNodeFactory.instance.arrayNode();
	    for (int i = 0; i < json.length(); i++) {
	        Object value;
	        try {
	            value = json.get(i);
	        } catch (JSONException e) {
	            throw new RuntimeException(e);
	        }
	        if (json.isNull(i))
	            ret.addNull();
	        else if (value instanceof String)
	            ret.add((String) value);
	        else if (value instanceof Integer)
	            ret.add((Integer) value);
	        else if (value instanceof Long)
	            ret.add((Long) value);
	        else if (value instanceof Double)
	            ret.add((Double) value);
	        else if (value instanceof Boolean)
	            ret.add((Boolean) value);
	        else if (value instanceof JSONObject)
	            ret.add(convertJsonFormat((JSONObject) value));
	        else if (value instanceof JSONArray)
	            ret.add(convertJsonFormat((JSONArray) value));
	        else
	            throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
	    }
	    return ret;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	//Fin declaración de métodos auxiliares
	////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////
	//Comienzo declaración métodos REST
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * El método mostrarOfertas hace la petición GET de las ofertas laborales existentes en la BD.
	 * @return Retorna la lista de las ofertas laborales existentes, la lista es del tipo Tco_ofertasDto para garantizar que solo 
	 * pasen los campos necesarios. Además recibe un código 200 y un status OK en caso de tener una petición exitosa
	 */
	@GetMapping("/generar")
	public ResponseEntity<List<Tco_ofertasDto>> mostrarOfertas(){
		List<Tco_ofertasDto> ofertas = tco_segmentacionService.mostrarOfertas();
		return new ResponseEntity<>(ofertas, HttpStatus.OK);
	}
	
	/**
	 * El método postOferta recibe la petición POST con la oferta laboral enviada por el usuario
	 * @param objeto El objeto que contiene el Id de la oferta laboral
	 * @throws JsonProcessingException
	 */
	@PostMapping("/generar")
	public void postOferta(@RequestBody Object objeto) throws JsonProcessingException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objeto);
		JSONObject new_json = new JSONObject(json);
		this.idOferta = new_json.getInt("idOferta");
		System.out.println(this.idOferta = new_json.getInt("idOferta"));
	}
	
	/**
	 * El método getCriterios es el que hace la petición GET de todos los posibles criterios de segmentación que puede usar el usuario
	 * @return Retorna la lista de listas de criterios junto con su tipo (si es un criterio que se puede implementar con un cuadro de texto,
	 * un select, un radio button, etc). Además recibe un código 200 y un status OK en caso de tener una petición exitosa.
	 */
	@GetMapping("/criterios")
	public ResponseEntity<ArrayList<ArrayList<String>>> getCriterios() {
		ArrayList<ArrayList<String>> criterios = tco_segmentacionService.listarCriterios();
		return new ResponseEntity<>(criterios, HttpStatus.OK);
	}
	
	/**
	 * El método postCriterios recibe la petición POST con los criterios elegidos por el usuario.
	 * @param objeto El objeto que contiene como atributos los criterios elegidos por el usuario.
	 * @return Retorna la lista de los criterios seleccionados, la lista es de tipo String.
	 */
	@PostMapping("/criterios")
	public List<String> postCriterios(@RequestBody Object objeto) throws JsonProcessingException{
		List<String> lista = new ArrayList<>();
		List<String> listaUpper = new ArrayList<>();
		
		//Mapeamos el objeto como un String y luego como un JSONObject para extraer los valores de sus atributos
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objeto);
		JSONObject new_json = new JSONObject(json);
		
		//Extraemos los valores de los atributos del JSONObject creado
		for(int i=1; i<=5; i++) {
			String criterio = new_json.getString("criterio"+i);
			if(criterio.equals("Nivel Formación")) {
				lista.add("nivel formacion");
			}
			else if(criterio.equals("Título Formación")) {
				lista.add("titulo formacion");
			}
			else {
				lista.add(criterio);
			}
		}
		
		//Formateamos los criterios para que coincidan con los nombres de los campos en la BD
		for(String criterio : lista) {
			listaUpper.add(criterio.replace(" ", "_").toUpperCase());
		}
		this.criteriosSeleccionados = listaUpper;
		List<String> listaCriterios = criteriosSeleccionados;
		List<String> listaTablas = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			String nombreTabla = tco_segmentacionService.nombreTabla(listaCriterios.get(i));
			listaTablas.add(nombreTabla);
		}
		return listaUpper;		
	}
	
	/**
	 * El método postPonderaciones recibe la petición POST con las ponderaciones elegidas por el usuario. 
	 * @param objeto El objeto que contiene como atributos las ponderaciones elegidas por el usuario.
	 * @return Retorna la lista de los enteros que fueron asignados como ponderación de cada criterio.
	 * @throws JsonProcessingException
	 */
	@PostMapping("/ponderaciones")
	public List<Integer> postPonderaciones(@RequestBody Object objeto) throws JsonProcessingException{
		List<Integer> lista = new ArrayList<>();
		
		//Mapeamos el objeto como un String y luego como un JSONObject para extraer los valores de sus atributos
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objeto);
		JSONObject new_json = new JSONObject(json);
		
		//Extraemos los valores de los atributos del JSONObject creado
		for(int i=1; i<=5; i++) {
			lista.add(new_json.getInt("ponderacion"+i));
		}
		this.ponderaciones = lista;
		return lista;
	}
	
	/**
	 * El método postPrioridades recibe la petición POST con las prioridades elegidas por el usuario para cada uno de los criterios.
	 * @param objeto El Objeto que contiene como atributos los criterios que son de tipo atributo y que a su vez contienen como atributos
	 * las prioridades seleccionadas.
	 * @return Un arreglo de listas que contienen cada una de las prioridades agrupadas según el criterio al que pertenecen
	 * @throws JsonProcessingException
	 */
	@PostMapping("/prioridades")
	public List<List<List<Object>>> postPrioridades(@RequestBody Object objeto) throws JsonProcessingException{
		List<List<List<Object>>> listaPpal = new ArrayList<List<List<Object>>>();
		
		//Mapeamos el objeto como un String y luego como un JSONObject para extraer los valores de sus atributos
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objeto);
		JSONObject new_json = new JSONObject(json);
		
		
		for(int i=1; i<=5; i++) {
			List<List<Object>> listaSec = new ArrayList<List<Object>>();
			Integer j = 1;
			Boolean flag = true;
			while(flag) {
				try {
					//Extraemos los valores de los atributos del JSONObject creado
					JSONArray array = new_json.getJSONObject("criterio"+i).getJSONArray("prioridad"+j);
					List<Object> listaTer = new ArrayList<Object>();
					for(int k=0; k < array.length(); k++) {
						if (array.get(k) instanceof String) {
							String jArray = array.getString(k);					
							listaTer.add(jArray);
						}
						else {
							Integer jArray = array.getInt(k);
							listaTer.add(jArray);
						}
					}
					j = j + 1;
					listaSec.add(listaTer);
				} catch (Exception e) {
					flag = false;
				}
			}
			listaPpal.add(listaSec);
		}
		this.prioridades = listaPpal;
		
		/*
		 * Una vez que el usuario ha elegido las prioridades para hacer la segmentación podemos proceder con la ejecución del cálculo
		 * de las ponderaciones (el proceso masivo). Entonces se llaman los métodos crearQueryPonderacion y crearSentenciaPonderacion
		 *  de la interfaz com.tcontrato.service/tco_segmentacionService para poder ejecutarlo.
		 */
		String queryStringEntrada = tco_segmentacionService.crearQueryPonderacion(criteriosSeleccionados,
				nombresTablas, ponderaciones, prioridades, idOferta);
		List<String> listaCriteriosElegidos = criteriosSeleccionados;
		String querySentenciaponderacion = tco_segmentacionService.crearSentenciaPonderacion(criteriosSeleccionados,
								ponderaciones, prioridades, idOferta);
		
		/*
		 * Con los strings de las consultas creados se procede a ejecutar el proceso masivo vía el método calcularPonderación de la
		 * interfaz com.tcontrato.service/tco_segmentacionService
		 */
		try {
			tco_segmentacionService.calcularPonderacion(queryStringEntrada, listaCriteriosElegidos, querySentenciaponderacion);
		} 
		catch (ClassNotFoundException e) {
				e.printStackTrace();
		} 
		catch (SQLException e) {
				e.printStackTrace();
		}
		System.out.println(queryStringEntrada);
		System.out.println(querySentenciaponderacion);		
		return listaPpal;
	}
	
	/**
	 * El método guardarReporte recibe la petición POST para actualizar el reporte de la oferta laboral elegida por el usuario.
	 * @return En caso de que la petición sea exitosa se recibe un status CREATED confirmando la actualización de la tabla.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParseException
	 */
	@PostMapping("/{idOferta}")
	public ResponseEntity<Tco_reportes> guardarReporte() throws ClassNotFoundException, SQLException, ParseException {
		
		/*
		 * Se inicializan los strings correspondientes que necesita el procedimiento almacenado generarReporte para llevar a cabo la
		 * construcción y almacenamiento del reporte de segmentación.
		 */
		
		String stringSentenciaReporte = tco_segmentacionService.crearSentenciaReporte(idOferta, criteriosSeleccionados, prioridades);
		String segmentacion = criteriosSeleccionados.toString().substring(1, criteriosSeleccionados.toString().length()-1) 
				+ "," + tco_segmentacionService.generarSegmentacion(stringSentenciaReporte);
		
		//Se genera el string que contiene el resultado de la segmentación, el string está conformado solo por las Ids de los candidatos
		String[] listaSegmentacion = segmentacion.split(",");		
		List<String> listaSegmentacionSinRepetidos = new ArrayList<String>();
		for(String componente : listaSegmentacion) {
			if(listaSegmentacionSinRepetidos.contains(componente) == false) {
				listaSegmentacionSinRepetidos.add(componente);
			}
		}
		segmentacion = String.join(",", listaSegmentacionSinRepetidos);
		System.out.println(segmentacion);
		long millis = System.currentTimeMillis();
		tco_segmentacionService.guardarReporte(segmentacion, idOferta, new Date(millis));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * El método mostrarCantidad recibe la petición POST con la cantidad de candidatos que quiere ver el usuario en el reporte
	 * @param objeto El Objeto que contiene el valor de la cantidad de candidatos a mostrar
	 * @throws JsonProcessingException
	 */
	@PostMapping("/cantidad")
	public void mostrarCantidad(@RequestBody Object objeto) throws JsonProcessingException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(objeto);
		JSONObject new_json = new JSONObject(json);
		this.cantidadMostrar = new_json.getInt("mostrarCantidad");
	}
	
	/**
	 * El método mostrarReporte hace la petición GET para obtener la lista de los candidatos a partir del string del reporte
	 * generado y almacenado en la BD.
	 * @param idOferta La Id de la oferta laboral para la que se quiere ver el reporte de segmentación.
	 * @return Retorna la lista de los candidatos ordenada según el resultado de la segmentación realizada.
	 * @throws IOException
	 */
	@GetMapping("/{idOferta}")
	@ResponseBody
	public ResponseEntity<List<Tco_reporteDeCandidatosDtos>> prueba(@PathVariable Integer idOferta) throws IOException {
		JSONArray jArray = tco_segmentacionService.generarReporte(idOferta);
		ArrayList<Tco_reporteDeCandidatosDtos> reporteDeCandidatos = new ArrayList<Tco_reporteDeCandidatosDtos>();
		if (jArray != null) {
			Integer limite = 0;
			if(this.cantidadMostrar > jArray.length()) {
				limite = jArray.length();
			}
			else {
				limite = this.cantidadMostrar;
			}
				for (int i=0; i < limite; i++){
				   JSONObject candidatoObject = jArray.getJSONObject(i);
				   JsonNode jsonNode = convertJsonFormat(candidatoObject);
				   ObjectMapper mapper = new ObjectMapper();
				   Tco_reporteDeCandidatosDtos myPojo = mapper.readValue(new TreeTraversingParser(jsonNode), Tco_reporteDeCandidatosDtos.class);
				   reporteDeCandidatos.add(myPojo);
			   }
			}		
		return new ResponseEntity<>(reporteDeCandidatos, HttpStatus.OK);
	}

	/*
	 * @GetMapping("/candidatos")
		public ResponseEntity<Set<Tco_candidatosDto>> getCandidatos() {
		Set<Tco_candidatosDto> candidatos = tco_segmentacionService.getAllCandidatos();
		return new ResponseEntity<>(candidatos, HttpStatus.OK);
		}
	 */
	
	////////////////////////////////////////////////////////////////////////////////
	//Fin declaración métodos REST
	////////////////////////////////////////////////////////////////////////////////

}