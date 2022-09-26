package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the prioridad database table.
 * 
 */
@Entity
@Table(name="prioridad")
@NamedQuery(name="Prioridad.findAll", query="SELECT p FROM Prioridad p")
public class Prioridad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_pri", unique=true, nullable=false)
	private Integer idPri;

	@Column(name="pri_intencidad")
	private Integer priIntencidad;

	@Column(name="pri_nombre", length=20)
	private String priNombre;

	//bi-directional many-to-one association to RegUsuIn
	@OneToMany(mappedBy="prioridad")
	private List<RegUsuIn> regUsuIns;

	public Prioridad() {
	}

	public Integer getIdPri() {
		return this.idPri;
	}

	public void setIdPri(Integer idPri) {
		this.idPri = idPri;
	}

	public Integer getPriIntencidad() {
		return this.priIntencidad;
	}

	public void setPriIntencidad(Integer priIntencidad) {
		this.priIntencidad = priIntencidad;
	}

	public String getPriNombre() {
		return this.priNombre;
	}

	public void setPriNombre(String priNombre) {
		this.priNombre = priNombre;
	}

	public List<RegUsuIn> getRegUsuIns() {
		return this.regUsuIns;
	}

	public void setRegUsuIns(List<RegUsuIn> regUsuIns) {
		this.regUsuIns = regUsuIns;
	}

	public RegUsuIn addRegUsuIn(RegUsuIn regUsuIn) {
		getRegUsuIns().add(regUsuIn);
		regUsuIn.setPrioridad(this);

		return regUsuIn;
	}

	public RegUsuIn removeRegUsuIn(RegUsuIn regUsuIn) {
		getRegUsuIns().remove(regUsuIn);
		regUsuIn.setPrioridad(null);

		return regUsuIn;
	}

}