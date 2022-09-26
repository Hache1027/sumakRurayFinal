package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reg_usu_ins database table.
 * 
 */
@Entity
@Table(name="reg_usu_ins")
@NamedQuery(name="RegUsuIn.findAll", query="SELECT r FROM RegUsuIn r")
public class RegUsuIn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="usu_ins_id", unique=true, nullable=false)
	private Integer usuInsId;

	//bi-directional many-to-one association to Prioridad
	@ManyToOne
	@JoinColumn(name="id_pri")
	private Prioridad prioridad;

	//bi-directional many-to-one association to RegInsumo
	@ManyToOne
	@JoinColumn(name="ins_id")
	private RegInsumo regInsumo;

	//bi-directional many-to-one association to SegUsuario
	@ManyToOne
	@JoinColumn(name="seg_id_seg_usuario")
	private SegUsuario segUsuario;

	public RegUsuIn() {
	}

	public Integer getUsuInsId() {
		return this.usuInsId;
	}

	public void setUsuInsId(Integer usuInsId) {
		this.usuInsId = usuInsId;
	}

	public Prioridad getPrioridad() {
		return this.prioridad;
	}

	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}

	public RegInsumo getRegInsumo() {
		return this.regInsumo;
	}

	public void setRegInsumo(RegInsumo regInsumo) {
		this.regInsumo = regInsumo;
	}

	public SegUsuario getSegUsuario() {
		return this.segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

}