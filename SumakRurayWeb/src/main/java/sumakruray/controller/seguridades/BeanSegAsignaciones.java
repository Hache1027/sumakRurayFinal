package sumakruray.controller.seguridades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import sumakruray.controller.JSFUtil;
import sumakruray.model.core.entities.SegModulo;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegRolesAcceso;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanSegAsignaciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSeguridades managerSeguridades;
	
	private List<SegRol> listaRoles;
	private List<SegModulo> listaModulos;
	private int idSegRolSeleccionado;
	private List<SegRolesAcceso> listaAccesos;
	
	
	@Inject
	private BeanSegLogin beanSegLogin;
	
	public BeanSegAsignaciones() {
		
	}
	
	public String actionMenuAsignaciones() {
		listaRoles=managerSeguridades.findAllRoles();
		listaModulos=managerSeguridades.findAllModulos();
		return "asignaciones";
	}
	
	public void actionListenerSeleccionarRol() {
		listaAccesos=managerSeguridades.findAccesosByRoles(idSegRolSeleccionado);
	}
	
	public void actionListenerAsignarPerfil(int idSegPerfil) {
		try {
			SegUsuario persona =managerSeguridades.findByIdSegUsuario(beanSegLogin.getLoginDTO().getIdSegUsuario());
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			String usuarioCrea = persona.getNombres() + " " + persona.getApellidos();
			managerSeguridades.asignarPerfilRol(idSegRolSeleccionado, idSegPerfil,tiempo, usuarioCrea);
			listaAccesos=managerSeguridades.findAccesosByRoles(idSegRolSeleccionado);
			JSFUtil.crearMensajeINFO("Perfil asignado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerEliminarAsignacionModulo(int idSegAcceso) {
		try {
			managerSeguridades.eliminarAccesoRol(idSegAcceso);
			listaAccesos=managerSeguridades.findAccesosByRoles(idSegRolSeleccionado);
			JSFUtil.crearMensajeINFO("Asignación eliminada.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}


	public List<SegModulo> getListaModulos() {
		return listaModulos;
	}

	public void setListaModulos(List<SegModulo> listaModulos) {
		this.listaModulos = listaModulos;
	}

	public int getIdSegRolSeleccionado() {
		return idSegRolSeleccionado;
	}

	public void setIdSegRolSeleccionado(int idSegRolSeleccionado) {
		this.idSegRolSeleccionado = idSegRolSeleccionado;
	}

	public List<SegRolesAcceso> getListaAccesos() {
		return listaAccesos;
	}

	public void setListaAccesos(List<SegRolesAcceso> listaAccesos) {
		this.listaAccesos = listaAccesos;
	}

	public List<SegRol> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<SegRol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	

}
