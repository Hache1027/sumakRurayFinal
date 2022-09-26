package sumakruray.model.actividades.managers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

import sumakruray.model.core.entities.RegActividad;
import sumakruray.model.core.entities.RegCategoria;
import sumakruray.model.core.entities.RegInsumo;
import sumakruray.model.core.entities.SegPerfil;
import sumakruray.model.core.entities.SegRol;
import sumakruray.model.core.managers.ManagerDAO;
import sumakruray.model.seguridades.dtos.LoginDTO;

/**
 * Session Bean implementation class ManagerActividades
 */
@Stateless
@LocalBean
public class ManagerCategorias {

	@EJB
	private ManagerDAO mDAO;
	
    /**
     * Default constructor. 
     */
    public ManagerCategorias() {
        // TODO Auto-generated constructor stub
    }
    
    public List<RegCategoria> findAllCategorias(){
    	return mDAO.findAll(RegCategoria.class,"catId",true);
    }
    
    public void insertarCategoria(RegCategoria nuevoCatagoria) throws Exception {
    	mDAO.insertar(nuevoCatagoria);
    }
    
    //Actualizar Categoria
    public void actualizarCatagoria(RegCategoria edicionCatagoria) throws Exception {
    	RegCategoria categoria=(RegCategoria) mDAO.findById(RegCategoria.class, edicionCatagoria.getCatId());
		Timestamp tiempo=new Timestamp(System.currentTimeMillis());
		categoria.setFechaModi(tiempo);
		categoria.setCatNombre(edicionCatagoria.getCatNombre());
		categoria.setCatEstado(edicionCatagoria.getCatEstado());
    	mDAO.actualizar(categoria);
    }
    
    public List<RegInsumo> findInsumosByCatagoria(int idRegCatagoria){
    	List<RegInsumo> listado=mDAO.findWhere(RegInsumo.class, "o.regCategoria.catId="+idRegCatagoria, "o.insId");
    	return listado;
    }
    
    //Agregar Insumo a categoria
    public void agregarInsumo(RegInsumo nuevoInsumo) throws Exception {
    	RegInsumo insumo=(RegInsumo) mDAO.findById(RegInsumo.class, nuevoInsumo.getInsId());
		insumo.setRegCategoria(nuevoInsumo.getRegCategoria());
		insumo.setFechaModi(nuevoInsumo.getFechaModi());
    	mDAO.actualizar(insumo);
    }
    
    //Quitar Insumo de categoria
    public void quitarInsumo(RegInsumo nuevoInsumo) throws Exception {
    	RegInsumo insumo=(RegInsumo) mDAO.findById(RegInsumo.class, nuevoInsumo.getInsId());
		insumo.setRegCategoria(nuevoInsumo.getRegCategoria());
		insumo.setFechaModi(nuevoInsumo.getFechaModi());
    	mDAO.actualizar(insumo);
    }
	
}
