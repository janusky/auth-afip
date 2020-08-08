package auth;

import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * Permite la comunicación con Web Service de Autorización y Autenticación
 * (WSAA) de AFIP.
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 6 ago. 2020 17:11:53
 *
 */
public class AuthenticationService {
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	/**
	 * Devuelve una objeto {@link LoginResponse} con información necesaria para
	 * autenticarse con servicios específicos de AFIP.
	 * 
	 * @param config
	 *            {@link Properties}
	 * @return {@link LoginResponse}
	 */
	public LoginResponse login(Properties config) {
		String endpoint = config.getProperty("endpoint");
		String service = config.getProperty("service");
		String dstDN = config.getProperty("dstdn");

		String p12file = config.getProperty("keystore");
		String signer = config.getProperty("keystore-signer");
		String p12pass = config.getProperty("keystore-password");

		// Set the keystore used by SSL
		//System.setProperty("javax.net.ssl.trustStore", config.getProperty("trustStore", ""));
		//System.setProperty("javax.net.ssl.trustStorePassword", config.getProperty("trustStore_password", ""));

		Long TicketTime = new Long(config.getProperty("TicketTime", "36000"));

		try {
			// Create LoginTicketRequest_xml_cms
			byte[] LoginTicketRequest_xml_cms = WebServiceAutenticationAutorize.create_cms(p12file, p12pass, signer, dstDN, service,
					TicketTime);

			bypassSSL();

			// Invoke AFIP wsaa and get LoginTicketResponse
			String LoginTicketResponse = WebServiceAutenticationAutorize.invoke_wsaa(LoginTicketRequest_xml_cms, endpoint);

			// Get token & sign from LoginTicketResponse
			Reader tokenReader = new StringReader(LoginTicketResponse);
			Document tokenDoc = new SAXReader(false).read(tokenReader);

			String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
			String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
			String generationTime = tokenDoc.valueOf("/loginTicketResponse/header/generationTime");
			String expirationTime = tokenDoc.valueOf("/loginTicketResponse/header/expirationTime");
			String asXML = tokenDoc.asXML();

			LoginResponse loginResponse = new LoginResponse(token, sign);
			Date create = DATE_FORMAT.parse(generationTime);
			loginResponse.setCreate(create);
			Date expiration = DATE_FORMAT.parse(expirationTime);
			loginResponse.setExpiration(expiration);
			loginResponse.setAsXML(asXML);

			return loginResponse;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static void bypassSSL() {
		// BaseWLSSLAdapter.setStrictCheckingDefault(false);
		// SSLAdapterFactory.getDefaultFactory().createSSLAdapter();
		System.setProperty("org.apache.axis.components.net.SecureSocketFactory",
				"org.apache.axis.components.net.SunFakeTrustSocketFactory");
	}
}
