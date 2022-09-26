package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the reg_tipo database table.
 * 
 */
@Entity
@Table(name="reg_tipo")
@NamedQuery(name="RegTipo.findAll", query="SELECT r FROM RegTipo r")
public class RegTipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tip_id", unique=true, nullable=false)
	private Integer tipId;

	@Column(name="tip_codigo", length=5)
	private String tipCodigo;

	@Column(name="tip_nombre", length=20)
	private String tipNombre;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="regTipo")
	private List<Detalle> detalles;

	//bi-directional many-to-one association to RegSolicitud
	@OneToMany(mappedBy="regTipo")
	private List<RegSolicitud> regSolicituds;

	public RegTipo() {
	}

	public Integer getTipId() {
		return this.tipId;
	}

	public void setTipId(Integer tipId) {
		this.tipId = tipId;
	}

	public String getTipCodigo() {
		return this.tipCodigo;
	}

	public void setTipCodigo(String tipCodigo) {
		this.tipCodigo = tipCodigo;
	}

	public String getTipNombre() {
		return this.tipNombre;
	}

	public void setTipNombre(String tipNombre) {
		this.tipNombre = tipNombre;
	}

	public List<Detalle> getDetalles() {
		return this.detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public Detalle addDetalle(Detalle detalle) {
		getDetalles().add(detalle);
		detalle.setRegTipo(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setRegTipo(null);

		return detalle;
	}

	public List<RegSolicitud> getRegSolicituds() {
		return this.regSolicituds;
	}

	public void setRegSolicituds(List<RegSolicitud> regSolicituds) {
		this.regSolicituds = regSolicituds;
	}

	public RegSolicitud addRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().add(regSolicitud);
		regSolicitud.setRegTipo(this);

		return regSolicitud;
	}

	public RegSolicitud removeRegSolicitud(RegSolicitud regSolicitud) {
		getRegSolicituds().remove(regSolicitud);
		regSolicitud.setRegTipo(null);

		return regSolicitud;
	}

}