package sumakruray.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the reg_cod_solicitud database table.
 * 
 */
@Entity
@Table(name="reg_cod_solicitud")
@NamedQuery(name="RegCodSolicitud.findAll", query="SELECT r FROM RegCodSolicitud r")
public class RegCodSolicitud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cod_sol_id", unique=true, nullable=false)
	private Integer codSolId;

	@Column(name="cod_anio", length=4)
	private String codAnio;

	@Column(name="cod_ult_numero")
	private Integer codUltNumero;

	public RegCodSolicitud() {
	}

	public Integer getCodSolId() {
		return this.codSolId;
	}

	public void setCodSolId(Integer codSolId) {
		this.codSolId = codSolId;
	}

	public String getCodAnio() {
		return this.codAnio;
	}

	public void setCodAnio(String codAnio) {
		this.codAnio = codAnio;
	}

	public Integer getCodUltNumero() {
		return this.codUltNumero;
	}

	public void setCodUltNumero(Integer codUltNumero) {
		this.codUltNumero = codUltNumero;
	}

}