package sumakruray.controller.seguridades;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import at.favre.lib.crypto.bcrypt.BCrypt;
import sumakruray.controller.JSFUtil;
import sumakruray.model.core.entities.SegDependencia;
import sumakruray.model.core.entities.SegModulo;
import sumakruray.model.core.entities.SegPerfil;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegRolesAcceso;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.core.managers.ManagerDAO;
import sumakruray.model.core.utils.ModelUtil;
import sumakruray.model.seguridades.dtos.LoginDTO;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

import sumakruray.metodos.MetodosSeguridades;

@Named
@SessionScoped
public class BeanSegLogin implements Serializable {

	private static final long serialVersionUID = 1L;
	private String correo;
	private String clave;
	private LoginDTO loginDTO;
	private String direccionIP;

	private MetodosSeguridades metodos;

	@EJB
	private ManagerDAO mDAO;

	@EJB
	private ManagerSeguridades mSeguridades;

	public BeanSegLogin() {

	}

	@PostConstruct
	public void inicializar() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String agente = req.getHeader("user-agent");
		String ipAddress = req.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = req.getRemoteAddr();
		}
		direccionIP = ipAddress;
		metodos = new MetodosSeguridades();
	}

	public String actionLogin() {
		try {
			loginDTO = login(correo, clave, direccionIP);
			// loginDTO.setDireccionIP(direccionIP);
			return "menu?faces-redirect=true";
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	// Prueba
	public LoginDTO login(String correo, String contrasenia, String direccionIP) throws Exception {
		// crear DTO:
		// String correo = new String(correo1);
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setCorreo(correo);
		loginDTO.setDireccionIP(direccionIP);
		List<SegUsuario> listausuarios = new ArrayList<>();
		SegUsuario usuario = new SegUsuario();

		if (ModelUtil.isEmpty(contrasenia)) {
			throw new Exception("Debe indicar una contraseña");
		}

		listausuarios = (List<SegUsuario>) mDAO.findAll(SegUsuario.class);

		for (SegUsuario segUsuario : listausuarios) {
			if (correo.equals(segUsuario.getCorreo())) {
				usuario = (SegUsuario) mDAO.findById(SegUsuario.class, segUsuario.getIdSegUsuario());
				loginDTO.setIdSegUsuario(segUsuario.getIdSegUsuario());
			}
		}

		if (usuario.getCorreo() == null) {
			throw new Exception("No existe ningún usuario con este CORREO");
		}

		//BCrypt.Result resultado = BCrypt.verifyer().verify(contrasenia.toCharArray(), usuario.getContrasenia());
		//resultado.verified
		if (metodos.verificarPassword(contrasenia, usuario.getContrasenia())) {
			if (usuario.getEstado() == false) {
				throw new Exception("El usuario esta desactivado.");
			}

			loginDTO.setCorreo(usuario.getCorreo());
			// aqui puede realizarse el envio de correo de notificacion.

			// obtener la lista de perfiles a los que tiene acceso:
			List<SegRolesAcceso> listaAccesos = mSeguridades.findAccesosByRoles(usuario.getSegRol().getIdSegRol());
			for (SegRolesAcceso accesos : listaAccesos) {
				SegPerfil perfil = accesos.getSegPerfil();
				loginDTO.getListaPerfiles().add(perfil);
			}
			return loginDTO;
		}
		throw new Exception("Error en credenciales");
	}

	public String actionMenu(String ruta) {
		return ruta + "?faces-redirect=true";
	}

	public String actionCerrarSesion() {
		mSeguridades.cerrarSesion(loginDTO.getCorreo());
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login?faces-redirect=true";
	}

	public void actionVerificarLogin() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String requestPath = ec.getRequestPathInfo();

		// primero validamos si loginDTO aun no se ha inicializado es porque
		// el usuario aun no ha pasado por la pantalla de login:

		if (loginDTO == null || loginDTO.getCorreo() == "") {
			try {
				mSeguridades.accesoNoPermitido("", requestPath);
				ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// Extraemos la parte inicial de la ruta a la que se esta accediendo:
		String rutaUsuario = requestPath.substring(1);
		rutaUsuario = rutaUsuario.substring(0, rutaUsuario.lastIndexOf("/"));
		// validacion de la ruta de acceso:
		boolean verificado = false;
		for (SegPerfil perfil : loginDTO.getListaPerfiles()) {
			// extraemos el inicio de la ruta (nombre de la carpeta):
			String rutaPerfil = perfil.getRutaAcceso();
			rutaPerfil = rutaPerfil.substring(0, rutaPerfil.indexOf("/menu"));
			// verificamos con la ruta a la que se estÃ¡ accediendo:
			if (rutaUsuario.equals(rutaPerfil)) {
				verificado = true;
				break;
			}
		}
		try {
			if (verificado == false) {
				mSeguridades.accesoNoPermitido(loginDTO.getCorreo(), requestPath);
				ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void actionVerificarOnlyLogin() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String requestPath = ec.getRequestPathInfo();

		// primero validamos si loginDTO aun no se ha inicializado es porque
		// el usuario aun no ha pasado por la pantalla de login:

		if (loginDTO == null || loginDTO.getCorreo() == "") {
			try {
				mSeguridades.accesoNoPermitido("", requestPath);
				ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

	}

	public void actionListenerInicialiarDemo() {
		try {
			String mensaje = inicializarDemo();
			JSFUtil.crearMensajeINFO(mensaje);
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Funcion de inicializacion de datos de usuarios.
	 */
	public String inicializarDemo() throws Exception {
		int idSegUsuarioAdmin = 0;
		int idSegRolAdmin = 0;
		String mensaje = "";// mensaje que se enviara al metodo que invoca a esta funcion.
		// buscar el usuario admin (id igual a 1)
		System.out.print("inicio");
		SegUsuario admin = (SegUsuario) mDAO.findById(SegUsuario.class, 1);
		SegRol rol = (SegRol) mDAO.findById(SegRol.class, 1);
		System.out.print("rol");
		SegDependencia dependencia = (SegDependencia) mDAO.findById(SegDependencia.class, 1);
		System.out.print("dependencia");

		Timestamp tiempo = new Timestamp(System.currentTimeMillis());
		// actividad.setFechaModi(tiempo);
		if (rol == null) {
			// creacion del usuario admin:
			rol = new SegRol();
			rol.setRolDescripcion("Administrador");
			rol.setRolCodigo("AD");
			rol.setEstado(true);
			rol.setFechaCreacion(tiempo);
			rol.setUsuarioCrea("INICIALIZACIÓN");
			mDAO.insertar(rol);

			idSegRolAdmin = rol.getIdSegRol();
		}
		if (dependencia == null) {
			// creacion del usuario admin:
			dependencia = new SegDependencia();
			dependencia.setDepDescripcion("Jefatura de Informática y Conectividad");
			dependencia.setDepCodigo("JIC");
			dependencia.setFechaCreacion(tiempo);
			dependencia.setUsuarioCrea("INICIALIZACIÓN");
			dependencia.setEstado(true);
			mDAO.insertar(dependencia);
		}
		// Creación de usuario
		if (admin == null) {
			admin = new SegUsuario();
			admin.setApellidos("admin");
			admin.setCorreo("admin@sumakruray.com");
			admin.setNombres("admin");
			admin.setCedula("100500472");
			admin.setTelefono("0994625468");
			admin.setDireccion("Otavalo");
			admin.setSegRol(rol);
			admin.setSegDependencia(dependencia);
			admin.setEstado(true);
			admin.setContrasenia(metodos.encriparPassword("admin"));
			admin.setUsuarioCrea("INICIALIZACIÓN");
			admin.setFechaCreacion(tiempo);
			mDAO.insertar(admin);

			idSegUsuarioAdmin = admin.getIdSegUsuario();

		} else {
			idSegUsuarioAdmin = admin.getIdSegUsuario();
			idSegRolAdmin = rol.getIdSegRol();
		}
		// mensaje="Id del usuario admin: "+idSegUsuarioAdmin;

		// verificar si ya existen los modulos iniciales:
		int idSegModuloSeguridades = 0;
		int idSegPerfilSegAdministrador = 0;
		List<SegModulo> modulos = mDAO.findAll(SegModulo.class);
		for (SegModulo m : modulos) {
			if (m.getNombreModulo().equals("Seguridades"))
				idSegModuloSeguridades = m.getIdSegModulo();
		}

		if (idSegModuloSeguridades == 0) {
			// inicializacion de modulo:
			SegModulo modulo = new SegModulo();
			modulo.setNombreModulo("Seguridades");
			modulo.setIcono("pi pi-lock");
			modulo.setFechaCreacion(tiempo);
			modulo.setUsuarioCrea("INICIALIZACIÓN");
			modulo.setEstado(true);
			mDAO.insertar(modulo);
			idSegModuloSeguridades = modulo.getIdSegModulo();
			// ahora creamos el perfil para el acceso del usuario:
			SegPerfil perfil = new SegPerfil();
			perfil.setNombrePerfil("Administrador");
			perfil.setRutaAcceso("seguridades/administrador/menu");
			perfil.setSegModulo(modulo);
			perfil.setFechaCreacion(tiempo);
			perfil.setUsuarioCrea("INICIALIZACIÓN");
			perfil.setEstado(true);
			mDAO.insertar(perfil);
			idSegPerfilSegAdministrador = perfil.getIdSegPerfil();
		} else {
			// si ya existe el modulo, buscamos el perfil de Administrador de seguridad:
			SegModulo m = (SegModulo) mDAO.findById(SegModulo.class, idSegModuloSeguridades);
			for (SegPerfil p : m.getSegPerfils()) {
				if (p.getRutaAcceso().equals("seguridades/administrador/menu"))
					idSegPerfilSegAdministrador = p.getIdSegPerfil();
			}
		}
		// asignacion de accesos:
		try {
			mSeguridades.asignarPerfilRol(idSegRolAdmin, idSegPerfilSegAdministrador);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// crearMensaje(FacesMessage.SEVERITY_INFO,"Inicializacion de bdd demo
		// terminada.",null);
		return mensaje + " Inicialización de bdd demo terminada.";
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public LoginDTO getLoginDTO() {
		return loginDTO;
	}

	public void setLoginDTO(LoginDTO loginDTO) {
		this.loginDTO = loginDTO;
	}

	public String getDireccionIP() {
		return direccionIP;
	}

	public void setDireccionIP(String direccionIP) {
		this.direccionIP = direccionIP;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

}
