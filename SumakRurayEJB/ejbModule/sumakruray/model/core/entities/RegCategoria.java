package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reg_categoria database table.
 * 
 */
@Entity
@Table(name="reg_categoria")
@NamedQuery(name="RegCategoria.findAll", query="SELECT r FROM RegCategoria r")
public class RegCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cat_id", unique=true, nullable=false)
	private Integer catId;

	@Column(name="cat_estado")
	private Boolean catEstado;

	@Column(name="cat_nombre", length=100)
	private String catNombre;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to RegInsumo
	@OneToMany(mappedBy="regCategoria")
	private List<RegInsumo> regInsumos;

	public RegCategoria() {
	}

	public Integer getCatId() {
		return this.catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Boolean getCatEstado() {
		return this.catEstado;
	}

	public void setCatEstado(Boolean catEstado) {
		this.catEstado = catEstado;
	}

	public String getCatNombre() {
		return this.catNombre;
	}

	public void setCatNombre(String catNombre) {
		this.catNombre = catNombre;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaModi() {
		return this.fechaModi;
	}

	public void setFechaModi(Timestamp fechaModi) {
		this.fechaModi = fechaModi;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<RegInsumo> getRegInsumos() {
		return this.regInsumos;
	}

	public void setRegInsumos(List<RegInsumo> regInsumos) {
		this.regInsumos = regInsumos;
	}

	public RegInsumo addRegInsumo(RegInsumo regInsumo) {
		getRegInsumos().add(regInsumo);
		regInsumo.setRegCategoria(this);

		return regInsumo;
	}

	public RegInsumo removeRegInsumo(RegInsumo regInsumo) {
		getRegInsumos().remove(regInsumo);
		regInsumo.setRegCategoria(null);

		return regInsumo;
	}

}