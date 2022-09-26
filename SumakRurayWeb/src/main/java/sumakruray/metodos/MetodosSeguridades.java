package sumakruray.metodos;

import java.io.Serializable;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class MetodosSeguridades implements Serializable {
	private static final long serialVersionUID = 1L;

	public String encriparPassword(String Contrasenia) {
		return BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(12, Contrasenia.toCharArray());
	}

	public boolean verificarPassword(String contrasenia, String encriptada) {
		BCrypt.Result resultado = BCrypt.verifyer().verify(contrasenia.toCharArray(), encriptada);
		return resultado.verified;
	}

}
