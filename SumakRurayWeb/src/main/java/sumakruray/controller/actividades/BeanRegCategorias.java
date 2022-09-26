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
import sumakruray.model.core.entities.RegCategoria;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.SegModulo;
import sumakruray.model.core.entities.SegPerfil;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.entities.SegUsuario;
import sumakruray.model.seguridades.managers.ManagerSeguridades;

@Named
@SessionScoped
public class BeanRegCategorias implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ManagerCategorias managerCategorias;
	
	@EJB
	private ManagerInsumos managerInsumos;
	
	@EJB
	private ManagerSeguridades managerSeguridades;
	
	private List<RegCategoria> listaCategorias;
	private RegCategoria nuevaCategoria;
	private RegCategoria edicionCategoria;
	private List<RegInsumo> listaInsumos;
	private RegCategoria categoriaSeleccionado;
	private RegInsumo nuevoInsumo;
	private int idInsumoSeleccionado;
	private List<RegInsumo> listaInsumocompleta;
	
	@Inject
	private BeanSegLogin beanSegLogin;
	
	public BeanRegCategorias() {
		
	}
	
	@PostConstruct
	public void inicializar() {
		nuevaCategoria = new RegCategoria();
		edicionCategoria = new RegCategoria();
		listaCategorias = managerCategorias.findAllCategorias();
		listaInsumocompleta = managerInsumos.findAllInsumos();
	}
	
	public String actionMenuCategorias() {
		listaCategorias = managerCategorias.findAllCategorias();
		listaInsumocompleta = managerInsumos.findAllInsumos();
		return "categorias";
	}

	public void actionListenerInsertarNuevaCategoria() {
		try {
	    	//Encontrar datos persona
	    	int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
	    	Timestamp tiempo=new Timestamp(System.currentTimeMillis());
	    	SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
	    	
	    	nuevaCategoria.setFechaCreacion(tiempo);
	    	nuevaCategoria.setUsuarioCrea(persona.getNombres()+" "+persona.getApellidos());
	    	nuevaCategoria.setCatEstado(true);
	    	//nuevaCategoria.setUltimaModi("Creación");
			managerCategorias.insertarCategoria(nuevaCategoria);
			listaCategorias = managerCategorias.findAllCategorias();
			nuevaCategoria = new RegCategoria();
			JSFUtil.crearMensajeINFO("Categoría creada correctamente");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerActualizarEdicionCategoria() {
		try {
			managerCategorias.actualizarCatagoria(edicionCategoria);
			listaCategorias = managerCategorias.findAllCategorias();
			JSFUtil.crearMensajeINFO("Categoría actualizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void actionListenerCargarCategoria(RegCategoria categoria) {
		edicionCategoria=categoria;
	}
	
	public String actionCargarInsumos(RegCategoria categoria) {
		listaInsumos=managerCategorias.findInsumosByCatagoria(categoria.getCatId());
		categoriaSeleccionado=categoria;
		return "insumocategorias";
	}
	
	public void actionListenerAgregarInsumo() {
		try {
			nuevoInsumo = (RegInsumo) managerInsumos.findByIdInsumo(idInsumoSeleccionado);
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			nuevoInsumo.setRegCategoria(categoriaSeleccionado);
			Timestamp tiempo=new Timestamp(System.currentTimeMillis());
	    	SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
			
			nuevoInsumo.setFechaModi(tiempo);
			managerCategorias.agregarInsumo(nuevoInsumo);
			listaInsumos=managerCategorias.findInsumosByCatagoria(categoriaSeleccionado.getCatId());
			JSFUtil.crearMensajeINFO("INSUMO agregado.");
			idInsumoSeleccionado = 0;
			nuevoInsumo=new RegInsumo();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerQuitarInsumo(RegInsumo insumo) {
		try {
			nuevoInsumo = insumo;
			int id_user = beanSegLogin.getLoginDTO().getIdSegUsuario();
			nuevoInsumo.setRegCategoria(null);
			Timestamp tiempo=new Timestamp(System.currentTimeMillis());
	    	SegUsuario persona = managerSeguridades.findByIdSegUsuario(id_user);
			
			nuevoInsumo.setFechaModi(tiempo);
			managerCategorias.quitarInsumo(nuevoInsumo);
			listaInsumos=managerCategorias.findInsumosByCatagoria(categoriaSeleccionado.getCatId());
			JSFUtil.crearMensajeINFO("INSUMO QUITADO.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<RegCategoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<RegCategoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public RegCategoria getNuevaCategoria() {
		return nuevaCategoria;
	}

	public void setNuevaCategoria(RegCategoria nuevaCategoria) {
		this.nuevaCategoria = nuevaCategoria;
	}

	public RegCategoria getEdicionCategoria() {
		return edicionCategoria;
	}

	public void setEdicionCategoria(RegCategoria edicionCategoria) {
		this.edicionCategoria = edicionCategoria;
	}

	public List<RegInsumo> getListaInsumos() {
		return listaInsumos;
	}

	public void setListaInsumos(List<RegInsumo> listaInsumos) {
		this.listaInsumos = listaInsumos;
	}

	public RegCategoria getCategoriaSeleccionado() {
		return categoriaSeleccionado;
	}

	public void setCategoriaSeleccionado(RegCategoria categoriaSeleccionado) {
		this.categoriaSeleccionado = categoriaSeleccionado;
	}

	public RegInsumo getNuevoInsumo() {
		return nuevoInsumo;
	}

	public void setNuevoInsumo(RegInsumo nuevoInsumo) {
		this.nuevoInsumo = nuevoInsumo;
	}

	public List<RegInsumo> getListaInsumocompleta() {
		return listaInsumocompleta;
	}

	public void setListaInsumocompleta(List<RegInsumo> listaInsumocompleta) {
		this.listaInsumocompleta = listaInsumocompleta;
	}

	public int getIdInsumoSeleccionado() {
		return idInsumoSeleccionado;
	}

	public void setIdInsumoSeleccionado(int idInsumoSeleccionado) {
		this.idInsumoSeleccionado = idInsumoSeleccionado;
	}
	
	
}
