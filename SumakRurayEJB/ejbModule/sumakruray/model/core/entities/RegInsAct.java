package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reg_ins_act database table.
 * 
 */
@Entity
@Table(name="reg_ins_act")
@NamedQuery(name="RegInsAct.findAll", query="SELECT r FROM RegInsAct r")
public class RegInsAct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ins_act_id", unique=true, nullable=false)
	private Integer insActId;

	//bi-directional many-to-one association to RegActividad
	@ManyToOne
	@JoinColumn(name="act_id")
	private RegActividad regActividad;

	//bi-directional many-to-one association to RegInsumo
	@ManyToOne
	@JoinColumn(name="ins_id")
	private RegInsumo regInsumo;

	public RegInsAct() {
	}

	public Integer getInsActId() {
		return this.insActId;
	}

	public void setInsActId(Integer insActId) {
		this.insActId = insActId;
	}

	public RegActividad getRegActividad() {
		return this.regActividad;
	}

	public void setRegActividad(RegActividad regActividad) {
		this.regActividad = regActividad;
	}

	public RegInsumo getRegInsumo() {
		return this.regInsumo;
	}

	public void setRegInsumo(RegInsumo regInsumo) {
		this.regInsumo = regInsumo;
	}

}