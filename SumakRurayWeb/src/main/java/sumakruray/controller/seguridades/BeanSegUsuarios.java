package sumakruray.controller.seguridades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.wildfly.security.password.interfaces.BCryptPassword;
import at.favre.lib.crypto.bcrypt.BCrypt;

import sumakruray.controller.JSFUtil;
import sumakruray.metodos.MetodosSeguridades;
import sumakruray.model.core.entities.SegContrasenia;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.utils.ModelUtil;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

/**
 * @author rfern
 *
 */

@Named
@SessionScoped
public class BeanSegUsuarios implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private ManagerSeguridades managerSeguridades;

	private List<SegUsuario> listaUsuarios;
	private SegUsuario nuevoUsuario;
	private SegUsuario edicionUsuario;
	// Variable usuario para cambiar la contraseña
	private SegUsuario con_usu;

	private List<SegRol> listaRoles;
	private List<SegDependencia> listaDependencias;
	private SegRol rolUsuarioNormal;

	private int idSegRolSeleccionado;
	private int idSegDependenciaSeleccionado;
	private String verificarContrasenia;

	private SegContrasenia cambioContrasenia;

	private MetodosSeguridades metodos;

	@Inject
	private BeanSegLogin beanSegLogin;

	public BeanSegUsuarios() {

	}

	@PostConstruct
	public void inicializar() {
		listaDependencias = managerSeguridades.findAllDependencias();
		nuevoUsuario = new SegUsuario();
		nuevoUsuario.setEstado(true);
		idSegDependenciaSeleccionado = 0;
		metodos = new MetodosSeguridades();
	}

	public String actionMenuUsuarios() {
		listaUsuarios = managerSeguridades.findAllUsuarios();
		listaRoles = managerSeguridades.findAllRoles();
		listaDependencias = managerSeguridades.findAllDependencias();
		verificarContrasenia = null;
		return "usuarios";
	}

	public void actionMenuRegistrarUsuario() {
		listaDependencias = managerSeguridades.findAllDependencias();
		nuevoUsuario = new SegUsuario();
		nuevoUsuario.setEstado(true);
		idSegDependenciaSeleccionado = 0;
	}

	public void actionMenuCambioContrasenia() {
		actionSeleccionarUsuarioContrasenia(beanSegLogin.getLoginDTO().getIdSegUsuario());
		cambioContrasenia = new SegContrasenia();
	}

	public void actionMenuCambioContraseniaAdmin(int IdUsuario) {
		actionSeleccionarUsuarioContrasenia(IdUsuario);
		cambioContrasenia = new SegContrasenia();
	}

	/**
	 * 
	 * @param idSegUsuario Usuario seleccionado
	 */
	public void actionListenerActivarDesactivarUsuario(int idSegUsuario) {
		try {
			managerSeguridades.activarDesactivarUsuario(idSegUsuario);
			listaUsuarios = managerSeguridades.findAllUsuarios();
			JSFUtil.crearMensajeINFO("Usuario activado/desactivado");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public String actionMenuNuevoUsuario() {
		nuevoUsuario = new SegUsuario();
		nuevoUsuario.setEstado(true);
		idSegDependenciaSeleccionado = 0;
		listaUsuarios = managerSeguridades.findAllUsuarios();
		listaDependencias = managerSeguridades.findAllDependencias();
		listaRoles = managerSeguridades.findAllRoles();
		return "usuarios_nuevo";
	}

	public void actionListenerInsertarNuevoUsuario() {
		try {
			if (nuevoUsuario.getContrasenia().equals(verificarContrasenia)) {
				if (!managerSeguridades.validarCorreo(nuevoUsuario.getCorreo())) {
					int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
					Timestamp tiempo = new Timestamp(System.currentTimeMillis());
					SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
					nuevoUsuario.setFechaCreacion(tiempo);
					nuevoUsuario.setUsuarioCrea(persona.getApellidos() + " " + persona.getNombres());
					nuevoUsuario.setSegRol(managerSeguridades.findByIdSegRol(idSegRolSeleccionado));
					nuevoUsuario.setContrasenia(metodos.encriparPassword(nuevoUsuario.getContrasenia()));
					nuevoUsuario
							.setSegDependencia(managerSeguridades.findByIdSegDependencia(idSegDependenciaSeleccionado));
					managerSeguridades.insertarUsuario(nuevoUsuario);
					listaUsuarios = managerSeguridades.findAllUsuarios();
					nuevoUsuario = new SegUsuario();
					nuevoUsuario.setEstado(true);
					idSegDependenciaSeleccionado = 0;
					idSegRolSeleccionado = 0;
					verificarContrasenia = null;
					JSFUtil.crearMensajeINFO("Usuario insertado correctamente.");
				} else {
					JSFUtil.crearMensajeWARN("ESTE CORREO YA ESTÁ UTILIZADO.");
				}
			} else {
				JSFUtil.crearMensajeWARN("LA CONTRASEÑA NO COINCIDE.");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Método donde se ingresan los datos del usuario que se está registrando

	public void actionListenerRegistrarseUsuario() {
		try {
			if (nuevoUsuario.getContrasenia().equals(verificarContrasenia)) {
				if (!managerSeguridades.validarCorreo(nuevoUsuario.getCorreo())) {
					Timestamp tiempo = new Timestamp(System.currentTimeMillis());
					nuevoUsuario.setFechaCreacion(tiempo);
					nuevoUsuario.setUsuarioCrea("Usuario Registrado");
					nuevoUsuario.setSegRol(managerSeguridades.findRolUsuarioNormal().get(0));
					nuevoUsuario.setContrasenia(metodos.encriparPassword(nuevoUsuario.getContrasenia()));
					nuevoUsuario
							.setSegDependencia(managerSeguridades.findByIdSegDependencia(idSegDependenciaSeleccionado));
					managerSeguridades.insertarUsuario(nuevoUsuario);
					listaUsuarios = managerSeguridades.findAllUsuarios();
					nuevoUsuario = new SegUsuario();
					nuevoUsuario.setEstado(true);
					idSegDependenciaSeleccionado = 0;
					idSegRolSeleccionado = 0;
					verificarContrasenia = null;
					JSFUtil.crearMensajeINFO("Usuario registrado correctamente");
				} else {
					JSFUtil.crearMensajeWARN("ESTE CORREO YA ESTÁ UTILIZADO");
				}
			} else {
				JSFUtil.crearMensajeWARN("LAS CONTRASEÑAS NO COINCIDEN");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param usuario
	 * @return String para redireccionar a la página de edición de usuario
	 */
	public void actionSeleccionarEdicionUsuario(SegUsuario usuario) {
		edicionUsuario = usuario;
		idSegDependenciaSeleccionado = edicionUsuario.getSegDependencia().getIdSegDependencia();
		idSegRolSeleccionado = edicionUsuario.getSegRol().getIdSegRol();
		verificarContrasenia = null;
		// return "usuarios_edicion";
	}

	public void actionListenerActualizarEdicionUsuario() {
		try {
			if (!managerSeguridades.validarCorreo(edicionUsuario.getCorreo(), edicionUsuario.getIdSegUsuario())) {
				Timestamp tiempo = new Timestamp(System.currentTimeMillis());
				edicionUsuario
						.setSegDependencia(managerSeguridades.findByIdSegDependencia(idSegDependenciaSeleccionado));
				edicionUsuario.setSegRol(managerSeguridades.findByIdSegRol(idSegRolSeleccionado));
				edicionUsuario.setFechaUltimaModificacion(tiempo);
				managerSeguridades.actualizarUsuario(edicionUsuario);
				listaUsuarios = managerSeguridades.findAllUsuarios();
				edicionUsuario = new SegUsuario();
				idSegDependenciaSeleccionado = 0;
				idSegRolSeleccionado = 0;
				JSFUtil.crearMensajeINFO("Usuario actualizado");
			} else {
				JSFUtil.crearMensajeWARN("ESE CORREO YA ESTÁ UTILIZADO");
			}
		} catch (

		Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	// Selecionar Roles
	public void actionListenerSeleccionarRol() {
		listaUsuarios = managerSeguridades.findAsignacionByRol(idSegRolSeleccionado);
	}

	/**
	 * Método para cambiar la contraseña(Usuarios)
	 */
	public void actionListenerCambiarContraseniaUsuario() {
		// comentado
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);

			if (metodos.verificarPassword(cambioContrasenia.getContraseniaAnterior(), con_usu.getContrasenia())) {
				if (cambioContrasenia.getContraseniaNueva().equals(verificarContrasenia)) {
					cambioContrasenia.setSegUsuario(persona);
					cambioContrasenia.setUsuarioCrea(persona.getApellidos() + " " + persona.getNombres());
					cambioContrasenia.setFechaCambio(tiempo);
					cambioContrasenia.setContraseniaNueva(metodos.encriparPassword(verificarContrasenia));
					cambioContrasenia.setContraseniaAnterior(con_usu.getContrasenia());
					con_usu.setContrasenia(cambioContrasenia.getContraseniaNueva());
					con_usu.setFechaUltimaModificacion(tiempo);
					managerSeguridades.inserterCambioContrasenia(cambioContrasenia);
					managerSeguridades.actualizarContraseniaUsuario(con_usu);
					cambioContrasenia = new SegContrasenia();
					verificarContrasenia = null;
					JSFUtil.crearMensajeINFO("Se ha cambiado la contraseña correctamente");
				} else {
					JSFUtil.crearMensajeWARN("Contraseña de confirmación incorrecta");
				}
			} else {
				JSFUtil.crearMensajeWARN("No coincide la contraseña actual");
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Método para cambiar la contraseña(Usuarios)
	 */
	public void actionListenerCambiarContraseniaAdmin() {
		// comentado
		try {
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			Timestamp tiempo = new Timestamp(System.currentTimeMillis());
			SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
			if (cambioContrasenia.getContraseniaNueva().equals(verificarContrasenia)) {
				cambioContrasenia.setSegUsuario(persona);
				cambioContrasenia.setUsuarioCrea(persona.getApellidos() + " " + persona.getNombres());
				cambioContrasenia.setFechaCambio(tiempo);
				cambioContrasenia.setContraseniaNueva(metodos.encriparPassword(verificarContrasenia));
				cambioContrasenia.setContraseniaAnterior(con_usu.getContrasenia());
				con_usu.setContrasenia(cambioContrasenia.getContraseniaNueva());
				con_usu.setFechaUltimaModificacion(tiempo);
				managerSeguridades.inserterCambioContrasenia(cambioContrasenia);
				managerSeguridades.actualizarContraseniaUsuario(con_usu);
				cambioContrasenia = new SegContrasenia();
				verificarContrasenia = null;
				con_usu = new SegUsuario();
				JSFUtil.crearMensajeINFO("Se ha cambiado la contraseña correctamente");
			} else {
				JSFUtil.crearMensajeWARN("Contraseña de confirmación incorrecta");
			}

		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para seleccionar usuario para el cambio de contraseña
	 * 
	 * @param idUsuario usuario al que se le va a actualizar la contraseña
	 */
	public void actionSeleccionarUsuarioContrasenia(int idUsuario) {
		try {
			con_usu = managerSeguridades.findByIdSegUsuario(idUsuario);
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<SegUsuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<SegUsuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public SegUsuario getNuevoUsuario() {
		return nuevoUsuario;
	}

	public void setNuevoUsuario(SegUsuario nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}

	public SegUsuario getEdicionUsuario() {
		return edicionUsuario;
	}

	public void setEdicionUsuario(SegUsuario edicionUsuario) {
		this.edicionUsuario = edicionUsuario;
	}

	public BeanSegLogin getBeanSegLogin() {
		return beanSegLogin;
	}

	public void setBeanSegLogin(BeanSegLogin beanSegLogin) {
		this.beanSegLogin = beanSegLogin;
	}

	public int getIdSegRolSeleccionado() {
		return idSegRolSeleccionado;
	}

	public void setIdSegRolSeleccionado(int idSegRolSeleccionado) {
		this.idSegRolSeleccionado = idSegRolSeleccionado;
	}

	public List<SegRol> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<SegRol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	public int getIdSegDependenciaSeleccionado() {
		return idSegDependenciaSeleccionado;
	}

	public void setIdSegDependenciaSeleccionado(int idSegDependenciaSeleccionado) {
		this.idSegDependenciaSeleccionado = idSegDependenciaSeleccionado;
	}

	public List<SegDependencia> getListaDependencias() {
		return listaDependencias;
	}

	public void setListaDependencias(List<SegDependencia> listaDependencias) {
		this.listaDependencias = listaDependencias;
	}

	public SegRol getRolUsuarioNormal() {
		return rolUsuarioNormal;
	}

	public void setRolUsuarioNormal(SegRol rolUsuarioNormal) {
		this.rolUsuarioNormal = rolUsuarioNormal;
	}

	public String getVerificarContrasenia() {
		return verificarContrasenia;
	}

	public void setVerificarContrasenia(String verificarContrasenia) {
		this.verificarContrasenia = verificarContrasenia;
	}

	public SegContrasenia getCambioContrasenia() {
		return cambioContrasenia;
	}

	public void setCambioContrasenia(SegContrasenia cambioContrasenia) {
		this.cambioContrasenia = cambioContrasenia;
	}

	public SegUsuario getCon_usu() {
		return con_usu;
	}

	public void setCon_usu(SegUsuario con_usu) {
		this.con_usu = con_usu;
	}

}
