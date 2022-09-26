package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the reg_path database table.
 * 
 */
@Entity
@Table(name="reg_path")
@NamedQuery(name="RegPath.findAll", query="SELECT r FROM RegPath r")
public class RegPath implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_path", unique=true, nullable=false)
	private Integer idPath;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_modi")
	private Timestamp fechaModi;

	@Column(name="pa_codigo", length=5)
	private String paCodigo;

	@Column(name="pa_direccion", length=100)
	private String paDireccion;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	public RegPath() {
	}

	public Integer getIdPath() {
		return this.idPath;
	}

	public void setIdPath(Integer idPath) {
		this.idPath = idPath;
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

	public String getPaCodigo() {
		return this.paCodigo;
	}

	public void setPaCodigo(String paCodigo) {
		this.paCodigo = paCodigo;
	}

	public String getPaDireccion() {
		return this.paDireccion;
	}

	public void setPaDireccion(String paDireccion) {
		this.paDireccion = paDireccion;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

}