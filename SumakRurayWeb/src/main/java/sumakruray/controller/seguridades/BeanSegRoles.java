package sumakruray.controller.seguridades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import sumakruray.controller.JSFUtil;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanSegRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<SegRol> listaRoles;
	private SegRol nuevoRol;
	private SegRol edicionRol;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanSegRoles() {
		// TODO Auto-generated constructor stub
	}

	// devolver lista de roles
	public String actionMenuRoles() {
		listaRoles = managerSeguridades.findAllRoles();
		return "roles";
	}

	// Lista de Roles
	public List<SegRol> getListaRoles() {
		return listaRoles;
	}

	// Crear Rol
	public String actionMenuNuevoRol() {
		nuevoRol = new SegRol();
		nuevoRol.setEstado(true);
		return "roles_nuevo";
	}

	public SegRol getNuevoRol() {
		return nuevoRol;
	}

	// Crear Rol
	public void actionListenerInsertarNuevoRol() {
		try {
			if (!managerSeguridades.validarCodigoRol(nuevoRol.getRolCodigo())) {
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				SegUsuario persona = managerSeguridades
						.findByIdSegUsuario(beanSegLogin.getLoginDTO().getIdSegUsuario());
				nuevoRol.setFechaCreacion(tiempo);
				nuevoRol.setUsuarioCrea(persona.getNombres() + " " + persona.getApellidos());
				managerSeguridades.insertarRol(nuevoRol);
				listaRoles = managerSeguridades.findAllRoles();
				nuevoRol = new SegRol();
				nuevoRol.setEstado(true);
				JSFUtil.crearMensajeINFO("Usuario insertado.");
			} else {
				JSFUtil.crearMensajeWARN("El código rol '" + nuevoRol.getRolCodigo() + "' ya fué utilizado");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Activar Desactivar Rol
	public void actionListenerActivarDesactivarRol(int idSegRol) {
		try {
			managerSeguridades.activarDesactivarRol(idSegRol);
			listaRoles = managerSeguridades.findAllRoles();
			JSFUtil.crearMensajeINFO("Rol activado/desactivado");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Actualizar Rol
	public String actionSeleccionarEdicionRol(SegRol rol) {
		edicionRol = rol;
		return "roles_edicion";
	}

	public void actionListenerActualizarEdicionRol() {
		try {
			if (!managerSeguridades.validarCodigoRol(edicionRol.getRolCodigo(), edicionRol.getIdSegRol())) {
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				edicionRol.setFechaModi(tiempo);
				managerSeguridades.actualizarRol(edicionRol);
				listaRoles = managerSeguridades.findAllRoles();
				JSFUtil.crearMensajeINFO("Rol actualizado");
			} else {
				JSFUtil.crearMensajeWARN("El código rol '" + edicionRol.getRolCodigo() + "' ya fué utilizado");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Set and Get

	public void setNuevoRol(SegRol nuevoRol) {
		this.nuevoRol = nuevoRol;
	}

	public SegRol getEdicionRol() {
		return edicionRol;
	}

	public void setEdicionRol(SegRol edicionRol) {
		this.edicionRol = edicionRol;
	}

	public void setListaRoles(List<SegRol> listaRoles) {
		this.listaRoles = listaRoles;
	}

}
