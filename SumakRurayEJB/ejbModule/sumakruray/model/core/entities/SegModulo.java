package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the seg_modulo database table.
 * 
 */
@Entity
@Table(name="seg_modulo")
@NamedQuery(name="SegModulo.findAll", query="SELECT s FROM SegModulo s")
public class SegModulo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_seg_modulo", unique=true, nullable=false)
	private Integer idSegModulo;

	@Column(nullable=false)
	private Boolean estado;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(nullable=false, length=50)
	private String icono;

	@Column(name="nombre_modulo", nullable=false, length=50)
	private String nombreModulo;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to SegPerfil
	@OneToMany(mappedBy="segModulo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<SegPerfil> segPerfils;

	public SegModulo() {
	}

	public Integer getIdSegModulo() {
		return this.idSegModulo;
	}

	public void setIdSegModulo(Integer idSegModulo) {
		this.idSegModulo = idSegModulo;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
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

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombreModulo() {
		return this.nombreModulo;
	}

	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<SegPerfil> getSegPerfils() {
		return this.segPerfils;
	}

	public void setSegPerfils(List<SegPerfil> segPerfils) {
		this.segPerfils = segPerfils;
	}

	public SegPerfil addSegPerfil(SegPerfil segPerfil) {
		getSegPerfils().add(segPerfil);
		segPerfil.setSegModulo(this);

		return segPerfil;
	}

	public SegPerfil removeSegPerfil(SegPerfil segPerfil) {
		getSegPerfils().remove(segPerfil);
		segPerfil.setSegModulo(null);

		return segPerfil;
	}

}