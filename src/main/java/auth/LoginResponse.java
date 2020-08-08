package auth;

import java.util.Date;

/**
 * Representa el objeto de autenticación con servicios proveídos por AFIP.
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 6 ago. 2020 17:27:46
 *
 */
public class LoginResponse {
	private String token;
	private String sign;
	private Date create;
	private Date expiration;

	/**
	 * XML que representa al objeto.
	 */
	private String asXML;

	public LoginResponse() {
	}

	public LoginResponse(String token, String sign) {
		super();
		this.token = token;
		this.sign = sign;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAsXML() {
		return asXML;
	}

	public void setAsXML(String asXML) {
		this.asXML = asXML;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
}
