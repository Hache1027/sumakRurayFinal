package sumakruray.controller.actividades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.actividades.managers.ManagerActividades;
import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.SegPerfil;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

/**
 * @author rfern
 * 
 */
@Named
@SessionScoped
public class BeanRegActividades implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ManagerActividades managerActividades;
	
	@EJB
	private ManagerSeguridades managerSeguridades;
	
	private List<RegActividad> listaActividades;
	private RegActividad nuevaActividad;
	private RegActividad edicionActividad;
	
	@Inject
	private BeanSegLogin beanSegLogin;
	
	public BeanRegActividades() {
		
	}
	
	@PostConstruct
	public void inicializar() {
		nuevaActividad = new RegActividad();
		listaActividades = managerActividades.findAllActividades();
		edicionActividad = new RegActividad();
	}
	/**
	 * Método para llenar los campos de UsuarioCrea, FechaCreacion y Estado.
	 * A continuación envía esos datos junto con los datos que haya ingrasado en la vista
	 * para insertar el objeto Actividad a la Base de Datos
	 */
	public void actionListenerInsertarNuevaActividad() {
		try {
	    	//Encontrar datos persona
	    	int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
	    	SegUsuario persona;
	    	Timestamp tiempo=new Timestamp(System.currentTimeMillis());
	    	
	    	persona = managerSeguridades.findByIdSegUsuario(id_user);
	    	nuevaActividad.setFechaCreacion(tiempo);
	    	nuevaActividad.setUsuarioCrea(persona.getNombres()+" "+persona.getApellidos());
			nuevaActividad.setActEstato(true);
			//nuevaActividad.setUltimaModi("Creación");
			managerActividades.insertarActividad(nuevaActividad);
			listaActividades = managerActividades.findAllActividades();
			nuevaActividad = new RegActividad();
			JSFUtil.crearMensajeINFO("Actividad creada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Método para actualizar una actividad seleccionada
	 */
	public void actionListenerActualizarEdicionActividad() {
		try {
			managerActividades.actualizarActividad(edicionActividad);
			listaActividades = managerActividades.findAllActividades();
			JSFUtil.crearMensajeINFO("Actividad actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerCargarActividad(RegActividad actividad) {
		edicionActividad=actividad;
	}


	public List<RegActividad> getListaActividades() {
		return listaActividades;
	}

	public void setListaActividades(List<RegActividad> listaActividades) {
		this.listaActividades = listaActividades;
	}

	public RegActividad getNuevaActividad() {
		return nuevaActividad;
	}

	public void setNuevaActividad(RegActividad nuevaActividad) {
		this.nuevaActividad = nuevaActividad;
	}

	public RegActividad getEdicionActividad() {
		return edicionActividad;
	}

	public void setEdicionActividad(RegActividad edicionActividad) {
		this.edicionActividad = edicionActividad;
	}

}
