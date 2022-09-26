package sumakruray.controller.actividades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import sumakruray.controller.JSFUtil;
import sumakruray.controller.seguridades.BeanSegLogin;
import sumakruray.model.actividades.managers.ManagerCategorias;
import sumakruray.model.actividades.managers.ManagerInsumos;
import sumakruray.model.core.entities.Prioridad;
import sumakruray.model.core.entities.RegCategoria;
import sumakruray.model.core.entities.RegInsAct;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.RegUsuIn;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanRegInsumos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ManagerInsumos managerInsumos;
	
	@EJB
	private ManagerSeguridades managerSeguridades;
	
	private List<RegInsumo> listaInsumos;
	private RegInsumo nuevaInsumo;
	private RegInsumo edicionInsumo;
	private int idRegInsumoSeleccionado;
	private List<RegInsAct> listaIns_actividad;
	private int idTecnicoSeleccionado;
	private int idPrioridadSeleccionada;
	private List<RegUsuIn> listaInsumosAsignados;
	private List<Prioridad> listaPrioridades;
	private List<SegUsuario> listaUsuarios;
	
	@Inject
	private BeanSegLogin beanSegLogin;
	
	public BeanRegInsumos() {
		
	}
	
	@PostConstruct
	public void inicializar() {
		listaInsumos = managerInsumos.findAllInsumos();
		nuevaInsumo = new RegInsumo();
		edicionInsumo = new RegInsumo();
		listaPrioridades = managerInsumos.findAllPrioridades();
		listaUsuarios=managerInsumos.findUsuariosTecnicos();
	}
	
	public String actionMenuResponsabilidades() {
		listaPrioridades = managerInsumos.findAllPrioridades();
		listaInsumos = managerInsumos.findAllInsumos();
		listaUsuarios=managerInsumos.findUsuariosTecnicos();
		return "responsabilidades";
	}

	public void actionListenerInsertarNuevoInsumo() {
		try {
	    	//Encontrar datos persona
	    	int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
	    	Timestamp tiempo=new Timestamp(System.currentTimeMillis());
	    	SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
	    	
	    	nuevaInsumo.setFechaCreacion(tiempo);
	    	nuevaInsumo.setUsuarioCrea(persona.getNombres()+" "+persona.getApellidos());
	    	nuevaInsumo.setInsEstado(true);
	    	//nuevaCategoria.setUltimaModi("Creación");
			managerInsumos.insertarInsumo(nuevaInsumo);
			listaInsumos = managerInsumos.findAllInsumos();
			nuevaInsumo = new RegInsumo();
			JSFUtil.crearMensajeINFO("Categoría creada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerSeleccionarUsuario() {
		listaIns_actividad=managerInsumos.findActividadesByInsumo(idRegInsumoSeleccionado);
	}
	
	public void actionListenerSeleccionarInsumo() {
		listaIns_actividad=managerInsumos.findActividadesByInsumo(idRegInsumoSeleccionado);
	}
	
	public void actionListenerActualizarEdicionInsumo() {
		try {
			managerInsumos.actualizarInsumo(edicionInsumo);
			listaInsumos = managerInsumos.findAllInsumos();
			JSFUtil.crearMensajeINFO("Insumo actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerAsignarActividadtoInsumo(int idRecActividad) {
		try {
			managerInsumos.asignarActividadToInsumo(idRegInsumoSeleccionado, idRecActividad);
			listaIns_actividad=managerInsumos.findActividadesByInsumo(idRegInsumoSeleccionado);
			JSFUtil.crearMensajeINFO("Actividad asignado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void actionListenerEliminarActividadInsumo(int idRegInsAct) {
		try {
			managerInsumos.eliminarInsAct(idRegInsAct);
			listaIns_actividad=managerInsumos.findActividadesByInsumo(idRegInsumoSeleccionado);
			JSFUtil.crearMensajeINFO("Asignación eliminada.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	
	public void actionListenerAsignarInsumo(int idInsumo) {
		try {
			managerInsumos.asignarInsumo(idTecnicoSeleccionado, idInsumo,idPrioridadSeleccionada);
			listaInsumosAsignados=managerInsumos.findInsumosByUsuario(idTecnicoSeleccionado);
			JSFUtil.crearMensajeINFO("INSUMO asignado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerEliminarAsignacionInsumo(int idInsumoTecnico) {
		try {
			managerInsumos.eliminarAsignacionInsumo(idInsumoTecnico);
			listaInsumosAsignados=managerInsumos.findInsumosByUsuario(idTecnicoSeleccionado);
			JSFUtil.crearMensajeINFO("Asignación de Insumo eliminada.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	
	public void actionListenerSeleccionarTecnico() {
		listaInsumosAsignados=managerInsumos.findInsumosByUsuario(idTecnicoSeleccionado);
	}
	
	
	public List<Prioridad> getListaPrioridades() {
		return listaPrioridades;
	}

	public void setListaPrioridades(List<Prioridad> listaPrioridades) {
		this.listaPrioridades = listaPrioridades;
	}

	public void actionListenerCargarInsumo(RegInsumo insumo) {
		edicionInsumo=insumo;
	}

	public List<RegInsumo> getListaInsumos() {
		return listaInsumos;
	}

	public void setListaInsumos(List<RegInsumo> listaInsumos) {
		this.listaInsumos = listaInsumos;
	}

	public RegInsumo getNuevaInsumo() {
		return nuevaInsumo;
	}

	public void setNuevaInsumo(RegInsumo nuevaInsumo) {
		this.nuevaInsumo = nuevaInsumo;
	}

	public RegInsumo getEdicionInsumo() {
		return edicionInsumo;
	}

	public void setEdicionInsumo(RegInsumo edicionInsumo) {
		this.edicionInsumo = edicionInsumo;
	}

	public int getIdRegInsumoSeleccionado() {
		return idRegInsumoSeleccionado;
	}

	public void setIdRegInsumoSeleccionado(int idRegInsumoSeleccionado) {
		this.idRegInsumoSeleccionado = idRegInsumoSeleccionado;
	}

	public List<RegInsAct> getListaIns_actividad() {
		return listaIns_actividad;
	}

	public void setListaIns_actividad(List<RegInsAct> listaIns_actividad) {
		this.listaIns_actividad = listaIns_actividad;
	}

	public int getIdTecnicoSeleccionado() {
		return idTecnicoSeleccionado;
	}

	public void setIdTecnicoSeleccionado(int idTecnicoSeleccionado) {
		this.idTecnicoSeleccionado = idTecnicoSeleccionado;
	}

	public int getIdPrioridadSeleccionada() {
		return idPrioridadSeleccionada;
	}

	public void setIdPrioridadSeleccionada(int idPrioridadSeleccionada) {
		this.idPrioridadSeleccionada = idPrioridadSeleccionada;
	}

	public List<RegUsuIn> getListaInsumosAsignados() {
		return listaInsumosAsignados;
	}

	public void setListaInsumosAsignados(List<RegUsuIn> listaInsumosAsignados) {
		this.listaInsumosAsignados = listaInsumosAsignados;
	}

	public List<SegUsuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<SegUsuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	
}
