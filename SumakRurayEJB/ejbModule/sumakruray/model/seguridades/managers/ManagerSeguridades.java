package sumakruray.model.seguridades.managers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.wildfly.security.password.interfaces.BCryptPassword;
import sumakruray.model.core.entities.SegContrasenia;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegModulo;
import sumakruray.model.core.entities.SegPerfil;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegRolesAcceso;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;
import sumakruray.model.core.utils.ModelUtil;
import sumakruray.model.seguridades.dtos.LoginDTO;

/**
 * Implementa la logica de manejo de usuarios y autenticacion. Funcionalidades
 * principales:
 * <ul>
 * <li>Verificacion de credenciales de usuario.</li>
 * <li>Asignacion de modulos a un usuario.</li>
 * </ul>
 * 
 * @author rfern
 * @author mrea
 */
@Stateless
@LocalBean
public class ManagerSeguridades {
	@EJB
	private ManagerDAO mDAO;

	// private BeanSegUsuarios beanUser;

	/**
	 * Default constructor.
	 */
	public ManagerSeguridades() {

	}

	/**
	 * Funcion de autenticacion mediante el uso de credenciales.
	 * 
	 * @param idSegUsuario El codigo del usuario que desea autenticarse.
	 * @param clave        La contrasenia.
	 * @param direccionIP  La direcciÃ³n IP V4 del cliente web.
	 * @return La ruta de acceso al sistema.
	 * @throws Exception
	 */

	public void cerrarSesion(String correo) {
		crearMensaje(FacesMessage.SEVERITY_INFO, "Cerrar sesión usuario: " + correo, null);
	}

	public void accesoNoPermitido(String correo, String ruta) {
		crearMensaje(FacesMessage.SEVERITY_WARN, "Usuario " + correo + " intento no autorizado a " + ruta, null);
	}

	public List<SegUsuario> findAllUsuarios() {
		return mDAO.findAll(SegUsuario.class, "idSegUsuario");
	}

	// Consulta a la Tabla de Rol
	public List<SegRol> findAllRoles() {
		return mDAO.findAll(SegRol.class, "rolDescripcion");
	}

	// Consulta a la Tabla de Dependecia
	public List<SegDependencia> findAllDependencias() {
		return mDAO.findAll(SegDependencia.class, "depDescripcion");
	}

	public SegUsuario findByIdSegUsuario(int idSegUsuario) throws Exception {
		return (SegUsuario) mDAO.findById(SegUsuario.class, idSegUsuario);
	}

	public SegRol findByIdSegRol(int idSegRol) throws Exception {
		return (SegRol) mDAO.findById(SegRol.class, idSegRol);
	}

	public SegDependencia findByIdSegDependencia(int idSegDependencia) throws Exception {
		return (SegDependencia) mDAO.findById(SegDependencia.class, idSegDependencia);
	}

	public void insertarUsuario(SegUsuario nuevoUsuario) throws Exception {
		mDAO.insertar(nuevoUsuario);
	}

	public void insertarRol(SegRol nuevoRol) throws Exception {
		mDAO.insertar(nuevoRol);
	}

	public void insertarDependencia(SegDependencia nuevoDependencia) throws Exception {
		mDAO.insertar(nuevoDependencia);
	}

	public void actualizarUsuario(SegUsuario edicionUsuario) throws Exception {
		SegUsuario usuario = (SegUsuario) mDAO.findById(SegUsuario.class, edicionUsuario.getIdSegUsuario());
		usuario.setApellidos(edicionUsuario.getApellidos());
		usuario.setContrasenia(edicionUsuario.getContrasenia());
		usuario.setCorreo(edicionUsuario.getCorreo());
		usuario.setCedula(edicionUsuario.getCedula());
		usuario.setNombres(edicionUsuario.getNombres());
		usuario.setTelefono(edicionUsuario.getTelefono());
		usuario.setDireccion(edicionUsuario.getDireccion());
		usuario.setSegDependencia(edicionUsuario.getSegDependencia());
		usuario.setSegRol(edicionUsuario.getSegRol());
		usuario.setFechaUltimaModificacion(edicionUsuario.getFechaUltimaModificacion());
		mDAO.actualizar(usuario);
	}

	// Actualizar Rol
	public void actualizarRol(SegRol edicionRol) throws Exception {
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, edicionRol.getIdSegRol());
		rol.setFechaModi(edicionRol.getFechaModi());
		rol.setRolCodigo(edicionRol.getRolCodigo());
		rol.setRolDescripcion(edicionRol.getRolDescripcion());
		mDAO.actualizar(rol);
	}

	// Actualizar Dependencia
	public void actualizarDependencia(SegDependencia edicionDependencia) throws Exception {
		SegDependencia dependencia = (SegDependencia) mDAO.findById(SegDependencia.class,
				edicionDependencia.getIdSegDependencia());
		dependencia.setFechaModi(edicionDependencia.getFechaModi());
		dependencia.setDepDescripcion(edicionDependencia.getDepDescripcion());
		mDAO.actualizar(dependencia);
	}

	public void activarDesactivarUsuario(int idSegUsuario) throws Exception {
		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		SegUsuario usuario = (SegUsuario) mDAO.findById(SegUsuario.class, idSegUsuario);
		if (idSegUsuario == 1)
			throw new Exception("No se puede desactivar al usuario administrador.");
		usuario.setFechaUltimaModificacion(tiempo);
		usuario.setEstado(!usuario.getEstado());
		mDAO.actualizar(usuario);
	}

	// Activar desactivar Rol
	public void activarDesactivarRol(int idSegRol) throws Exception {
		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, idSegRol);
		if (idSegRol == 1)
			throw new Exception("No se puede desactivar el Rol Administrador.");
		rol.setEstado(!rol.getEstado());
		rol.setFechaModi(tiempo);
		mDAO.actualizar(rol);
	}

	// Activar desactivar Dependencia
	public void activarDesactivarDependencia(int idSegDependencia) throws Exception {
		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		SegDependencia dependencia = (SegDependencia) mDAO.findById(SegDependencia.class, idSegDependencia);
		dependencia.setEstado(!dependencia.getEstado());
		dependencia.setFechaModi(tiempo);
		mDAO.actualizar(dependencia);
	}

	// Eliminar Rol
	public void eliminarRol(int idSegRol) throws Exception {
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, idSegRol);
		if (rol.getIdSegRol() == 1)
			throw new Exception("No se puede eliminar el usuario administrador.");

		// if(rol.getSegAsignacions().size()>0)
		// throw new Exception("No se puede elimininar el usuario porque tiene
		// asignaciones de módulos.");
		mDAO.eliminar(SegRol.class, rol.getIdSegRol());
	}

	public List<SegModulo> findAllModulos() {
		return mDAO.findAll(SegModulo.class, "nombreModulo");
	}

	public SegModulo insertarModulo(SegModulo nuevoModulo) throws Exception {
		SegModulo modulo = new SegModulo();
		modulo.setNombreModulo(nuevoModulo.getNombreModulo());
		modulo.setIcono(nuevoModulo.getIcono());
		modulo.setEstado(true);
		modulo.setFechaCreacion(nuevoModulo.getFechaCreacion());
		modulo.setUsuarioCrea(nuevoModulo.getUsuarioCrea());
		mDAO.insertar(modulo);
		return modulo;
	}
	
	public void actualizarModulo(SegModulo edicionModulo) throws Exception {
		SegModulo modulo = (SegModulo) mDAO.findById(SegModulo.class, edicionModulo.getIdSegModulo());
		modulo.setNombreModulo(edicionModulo.getNombreModulo());
		modulo.setIcono(edicionModulo.getIcono());
		modulo.setFechaModi(edicionModulo.getFechaModi());
		modulo.setEstado(true);
		mDAO.actualizar(modulo);
	}

	public List<SegRolesAcceso> findAccesosByRoles(int idSegRol) {
		String consulta = "o.segRol.idSegRol=" + idSegRol;
		List<SegRolesAcceso> listaAsignaciones = mDAO.findWhere(SegRolesAcceso.class, consulta,
				"o.segPerfil.segModulo.nombreModulo,o.segPerfil.nombrePerfil");
		return listaAsignaciones;
	}

	// Buscar Rol para usuarios
	public List<SegUsuario> findAsignacionByRol(int idSegRol) {
		String consulta = "o.segRol.idSegRol=" + idSegRol;
		List<SegUsuario> listaUsuarios = mDAO.findWhere(SegUsuario.class, consulta, null);
		return listaUsuarios;
	}

	// Buscar Rol Usuario Normal
	public List<SegRol> findRolUsuarioNormal() {
		String consulta = "o.rolCodigo='UN'";
		List<SegRol> listaRol = mDAO.findWhere(SegRol.class, consulta, null);
		return listaRol;
	}

	/**
	 * Permite asignar el acceso a un perfil del inventario de sistemas(Administrador)
	 * 
	 * @param idSegUsuario El codigo del usuario.
	 * @param idSegPerfil  El codigo del perfil que se va a asignar.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void asignarPerfilRol(int idRol, int idSegPerfil, Timestamp tiempo, String usuarioCrea) throws Exception {
		// Buscar los objetos primarios:
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, idRol);
		SegPerfil perfil = (SegPerfil) mDAO.findById(SegPerfil.class, idSegPerfil);
		// verificar si ya existe:
		String consulta = "o.segPerfil.idSegPerfil=" + idSegPerfil + " and o.segRol.idSegRol=" + idRol;
		List<SegRolesAcceso> rol_accesos = mDAO.findWhere(SegRolesAcceso.class, consulta, null);
		if (rol_accesos == null || rol_accesos.size() == 0) {
			// crear la relacion:
			SegRolesAcceso acceso = new SegRolesAcceso();
			acceso.setSegPerfil(perfil);
			acceso.setSegRol(rol);
			acceso.setFechaCreacion(tiempo);
			acceso.setUsuarioCrea(usuarioCrea);
			// guardar la asignacion:
			mDAO.insertar(acceso);

		} else {
			throw new Exception("Ya existe la asignación de rol/perfil (" + idRol + "/" + idSegPerfil + ")");
		}
	}

	
	/**
	 * Permite asignar el acceso a un perfil del inventario de sistemas(INICIALIZAR)
	 * 
	 * @param idSegUsuario El codigo del usuario.
	 * @param idSegPerfil  El codigo del perfil que se va a asignar.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void asignarPerfilRol(int idRol, int idSegPerfil) throws Exception {
		// Buscar los objetos primarios:
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, idRol);
		SegPerfil perfil = (SegPerfil) mDAO.findById(SegPerfil.class, idSegPerfil);
		// verificar si ya existe:
		String consulta = "o.segPerfil.idSegPerfil=" + idSegPerfil + " and o.segRol.idSegRol=" + idRol;
		List<SegRolesAcceso> rol_accesos = mDAO.findWhere(SegRolesAcceso.class, consulta, null);
		if (rol_accesos == null || rol_accesos.size() == 0) {
			// crear la relacion:
			SegRolesAcceso acceso = new SegRolesAcceso();
			acceso.setSegPerfil(perfil);
			acceso.setSegRol(rol);
			// guardar la asignacion:
			mDAO.insertar(acceso);

		} else {
			throw new Exception("Ya existe la asignación de rol/perfil (" + idRol + "/" + idSegPerfil + ")");
		}
	}
	public void eliminarAccesoRol(int idAccesoRol) throws Exception {
		mDAO.eliminar(SegRolesAcceso.class, idAccesoRol);
	}

	public List<SegPerfil> findPerfilesByModulo(int idSegModulo) {
		List<SegPerfil> listado = mDAO.findWhere(SegPerfil.class, "o.segModulo.idSegModulo=" + idSegModulo,
				"o.nombrePerfil");
		return listado;
	}

	public SegPerfil insertarPerfil(SegPerfil nuevoPerfil) throws Exception {
		SegPerfil perfil = new SegPerfil();
		perfil.setNombrePerfil(nuevoPerfil.getNombrePerfil());
		perfil.setRutaAcceso(nuevoPerfil.getRutaAcceso());
		perfil.setSegModulo(nuevoPerfil.getSegModulo());
		perfil.setFechaCreacion(nuevoPerfil.getFechaCreacion());
		perfil.setUsuarioCrea(nuevoPerfil.getUsuarioCrea());
		perfil.setEstado(true);
		mDAO.insertar(perfil);
		return perfil;
	}

	public void actualizarPerfil(SegPerfil edicionPerfil) throws Exception {
		SegPerfil perfil = (SegPerfil) mDAO.findById(SegPerfil.class, edicionPerfil.getIdSegPerfil());
		perfil.setNombrePerfil(edicionPerfil.getNombrePerfil());
		perfil.setRutaAcceso(edicionPerfil.getRutaAcceso());
		perfil.setFechaModi(edicionPerfil.getFechaModi());
		mDAO.actualizar(perfil);
	}

	public static void crearMensaje(Severity severidad, String mensaje, String detalle) {
		FacesMessage msg = new FacesMessage();
		msg.setSeverity(severidad);
		msg.setSummary(mensaje);
		msg.setDetail(detalle);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Busca si existe algún usuario con el mismo correo(nuevo usuario)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 */
	public boolean validarCorreo(String correo) {
		List<SegUsuario> listaUsuarios = findAllUsuarios();
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if (listaUsuarios.get(i).getCorreo().equals(correo)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Busca si existe algún usuario con el mismo correo(edicion usuario)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 */
	public boolean validarCorreo(String correo, int idUser) {
		List<SegUsuario> listaUsuarios = findAllUsuarios();
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if (listaUsuarios.get(i).getCorreo().equals(correo) && listaUsuarios.get(i).getIdSegUsuario() != idUser) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Busca si existe un rol anterior con un código igual(nuevo rol)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 * @return false cuando no encontró ninguna coincidencia
	 */
	public boolean validarCodigoRol(String codigo) {
		List<SegRol> listaRoles = findAllRoles();
		for (int i = 0; i < listaRoles.size(); i++) {
			if (listaRoles.get(i).getRolCodigo().equals(codigo)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Busca si existe un rol anterior con un código igual(rol edicion)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 * @return false cuando no encontró ninguna coincidencia
	 */
	public boolean validarCodigoRol(String codigo, int idRol) {
		List<SegRol> listaRoles = findAllRoles();
		for (int i = 0; i < listaRoles.size(); i++) {
			if (listaRoles.get(i).getRolCodigo().equals(codigo) && listaRoles.get(i).getIdSegRol() != idRol) {
				return true;
			}
		}
		return false;
	}
	
	

	/**
	 * Busca si existe una dependencia anterior con un código igual(nueva dependencia)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 * @return false cuando no encontró ninguna coincidencia
	 */
	public boolean validarCodigoDependencia(String codigo) {
		List<SegDependencia> listaRDependencias = findAllDependencias();
		for (int i = 0; i < listaRDependencias.size(); i++) {
			if (listaRDependencias.get(i).getDepCodigo().equals(codigo)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Busca si existe una dependencia anterior con un código igual(dependencia edicion)
	 * 
	 * @param correo Dato ingresado por el usuario al intentar registrarse
	 * @return true cuando encuentra una coincidencia
	 * @return false cuando no encontró ninguna coincidencia
	 */
	public boolean validarCodigoDependencia(String codigo, int idDep) {
		List<SegDependencia> listaRDependencias = findAllDependencias();
		for (int i = 0; i < listaRDependencias.size(); i++) {
			if (listaRDependencias.get(i).getDepCodigo().equals(codigo) && listaRDependencias.get(i).getIdSegDependencia() != idDep) {
				return true;
			}
		}
		return false;
	}

	// Método para insertar el cambiar la contraseña
	public void inserterCambioContrasenia(SegContrasenia nuevaContrasenia) throws Exception {
		mDAO.insertar(nuevaContrasenia);
	}

	/**
	 * @param edicionUsuario
	 * @throws Exception
	 */
	public void actualizarContraseniaUsuario(SegUsuario edicionUsuario) throws Exception {
		SegUsuario usuario = (SegUsuario) mDAO.findById(SegUsuario.class, edicionUsuario.getIdSegUsuario());
		usuario.setContrasenia(edicionUsuario.getContrasenia());
		usuario.setFechaUltimaModificacion(edicionUsuario.getFechaUltimaModificacion());
		mDAO.actualizar(usuario);
	}

}
