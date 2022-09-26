package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the seg_contrasenia database table.
 * 
 */
@Entity
@Table(name="seg_contrasenia")
@NamedQuery(name="SegContrasenia.findAll", query="SELECT s FROM SegContrasenia s")
public class SegContrasenia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_con", unique=true, nullable=false)
	private Integer idCon;

	@Column(name="contrasenia_anterior", length=100)
	private String contraseniaAnterior;

	@Column(name="contrasenia_nueva", length=100)
	private String contraseniaNueva;

	@Column(name="fecha_cambio")
	private Timestamp fechaCambio;

	@Column(length=300)
	private String motivo;

	@Column(name="usuario_crea", length=100)
	private String usuarioCrea;

	//bi-directional many-to-one association to SegUsuario
	@ManyToOne
	@JoinColumn(name="id_seg_usuario")
	private SegUsuario segUsuario;

	public SegContrasenia() {
	}

	public Integer getIdCon() {
		return this.idCon;
	}

	public void setIdCon(Integer idCon) {
		this.idCon = idCon;
	}

	public String getContraseniaAnterior() {
		return this.contraseniaAnterior;
	}

	public void setContraseniaAnterior(String contraseniaAnterior) {
		this.contraseniaAnterior = contraseniaAnterior;
	}

	public String getContraseniaNueva() {
		return this.contraseniaNueva;
	}

	public void setContraseniaNueva(String contraseniaNueva) {
		this.contraseniaNueva = contraseniaNueva;
	}

	public Timestamp getFechaCambio() {
		return this.fechaCambio;
	}

	public void setFechaCambio(Timestamp fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public SegUsuario getSegUsuario() {
		return this.segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

}