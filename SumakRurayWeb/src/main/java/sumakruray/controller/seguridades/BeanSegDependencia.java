package sumakruray.controller.seguridades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import sumakruray.controller.JSFUtil;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanSegDependencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<SegDependencia> listaDependencias;
	private SegDependencia nuevoDependencia;
	private SegDependencia edicionDependencia;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanSegDependencia() {
		// TODO Auto-generated constructor stub
	}

	// devolver lista de Dependencias
	public String actionMenuDependencias() {
		listaDependencias = managerSeguridades.findAllDependencias();
		return "dependencias";
	}

	// Lista de Dependencias
	public List<SegDependencia> getListaDependencias() {
		return listaDependencias;
	}

	// Crear Dependencia
	public String actionMenuNuevoDependencia() {
		nuevoDependencia = new SegDependencia();
		nuevoDependencia.setEstado(true);
		return "dependencias_nuevo";
	}

	public SegDependencia getNuevoDependencia() {
		return nuevoDependencia;
	}

	// Crear Dependencia
	public void actionListenerInsertarNuevoDependencia() {
		try {
			if (!managerSeguridades.validarCodigoDependencia(nuevoDependencia.getDepCodigo())) {
				int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
				nuevoDependencia.setFechaCreacion(tiempo);
				nuevoDependencia.setUsuarioCrea(persona.getApellidos() + " " + persona.getNombres());
				managerSeguridades.insertarDependencia(nuevoDependencia);
				listaDependencias = managerSeguridades.findAllDependencias();
				nuevoDependencia = new SegDependencia();
				nuevoDependencia.setEstado(true);
				JSFUtil.crearMensajeINFO("Dependencia insertada");
			} else {
				JSFUtil.crearMensajeWARN(
						"El código Dependencia '" + nuevoDependencia.getDepCodigo() + "' ya fué utilizado");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Activar Desactivar Dependencia
	public void actionListenerActivarDesactivarDependencia(int idSegDependencia) {
		try {
			managerSeguridades.activarDesactivarDependencia(idSegDependencia);
			listaDependencias = managerSeguridades.findAllDependencias();
			JSFUtil.crearMensajeINFO("Dependencia activado/desactivado");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Actualizar Dependencia
	public void actionSeleccionarEdicionDependencia(SegDependencia dependencia) {
		edicionDependencia = dependencia;
	}

	public void actionListenerActualizarEdicionDependencia() {
		try {
			if (!managerSeguridades.validarCodigoDependencia(edicionDependencia.getDepCodigo(), edicionDependencia.getIdSegDependencia())) {
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				edicionDependencia.setFechaModi(tiempo);
				managerSeguridades.actualizarDependencia(edicionDependencia);
				listaDependencias = managerSeguridades.findAllDependencias();
				JSFUtil.crearMensajeINFO("Dependencia actualizada");
			} else {
				JSFUtil.crearMensajeWARN(
						"El código Dependencia '" + edicionDependencia.getDepCodigo() + "' ya fué utilizado");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	// Set and Get

	public SegDependencia getEdicionDependencia() {
		return edicionDependencia;
	}

	public void setEdicionDependencia(SegDependencia edicionDependencia) {
		this.edicionDependencia = edicionDependencia;
	}

	public void setListaDependencias(List<SegDependencia> listaDependencias) {
		this.listaDependencias = listaDependencias;
	}

	public void setNuevoDependencia(SegDependencia nuevoDependencia) {
		this.nuevoDependencia = nuevoDependencia;
	}

}
