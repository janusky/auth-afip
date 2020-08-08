package auth;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.axis.encoding.XMLType;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/*
 * Es una copia de: afip_wsaa_client
 * 
 * 	http://www.afip.gob.ar/ws/WSAA/ejemplos/wsaa_client_java.tgz
 */
/**
 * Servicios de autenticación y autorización AFIP (WSAA).
 * 
 * @author janusky@gmail.com
 * @version 1.0 - 6 ago. 2020 16:38:54
 *
 */
public class WebServiceAutenticationAutorize {

	static String invoke_wsaa(byte[] LoginTicketRequest_xml_cms, String endpoint) throws Exception {
		String LoginTicketResponse = null;
		Service service = new Service();
		Call call = (Call) service.createCall();

		// Prepare the call for the Web service
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName("loginCms");
		call.addParameter("request", XMLType.XSD_STRING, ParameterMode.IN);
		call.setReturnType(XMLType.XSD_STRING);

		// Make the actual call and assign the answer to a String
		LoginTicketResponse = (String) call.invoke(new Object[] { Base64.encode(LoginTicketRequest_xml_cms) });
		return (LoginTicketResponse);
	}

	/**
	 * Create the CMS Message.
	 * 
	 * @param p12file
	 *            String
	 * @param p12pass
	 *            String
	 * @param signer
	 *            String
	 * @param dstDN
	 *            String
	 * @param service
	 *            String
	 * @param ticketTime
	 *            Long
	 * @return
	 */
	public static byte[] create_cms(String p12file, String p12pass, String signer, String dstDN, String service,
			Long ticketTime) throws Exception {
		PrivateKey pKey = null;
		X509Certificate pCertificate = null;
		byte[] asn1Cms = null;
		CertStore cstore = null;
		String SignerDN = null;

		// Manage Keys & Certificates
		// Create a keystore using keys from the pkcs#12 p12file
		KeyStore ks = KeyStore.getInstance("pkcs12");
		FileInputStream p12stream = new FileInputStream(p12file);
		ks.load(p12stream, p12pass.toCharArray());
		p12stream.close();
		// Get Certificate & Private key from KeyStore
		pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
		pCertificate = (X509Certificate) ks.getCertificate(signer);
		SignerDN = pCertificate.getSubjectDN().toString();
		// Create a list of Certificates to include in the final CMS
		ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
		certList.add(pCertificate);
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");

		// Create XML Message
		String loginTicketRequestXml = createLoginTicketRequest(SignerDN, dstDN, service, ticketTime);

		// Create a new empty CMS Message
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		// Add a Signer to the Message
		gen.addSigner(pKey, pCertificate, CMSSignedDataGenerator.DIGEST_SHA1);
		// Add the Certificate to the Message
		gen.addCertificatesAndCRLs(cstore);
		// Add the data (XML) to the Message
		CMSProcessable data = new CMSProcessableByteArray(loginTicketRequestXml.getBytes());
		// Add a Sign of the Data to the Message
		CMSSignedData signed = gen.generate(data, true, "BC");
		//
		asn1Cms = signed.getEncoded();
		return (asn1Cms);
	}

	/**
	 * Create XML Message for AFIP wsaa.
	 * 
	 * @param signerDN
	 *            String
	 * @param dstDN
	 *            String
	 * @param service
	 *            String
	 * @param ticketTime
	 *            Long
	 * @return String
	 */
	public static String createLoginTicketRequest(String signerDN, String dstDN, String service, Long ticketTime) {
		String loginTicketRequestXml;
		Date GenTime = new Date();
		GregorianCalendar gentime = new GregorianCalendar();
		GregorianCalendar exptime = new GregorianCalendar();
		String UniqueId = new Long(GenTime.getTime() / 1000).toString();

		exptime.setTime(new Date(GenTime.getTime() + ticketTime));

		XMLGregorianCalendarImpl XMLGenTime = new XMLGregorianCalendarImpl(gentime);
		XMLGregorianCalendarImpl XMLExpTime = new XMLGregorianCalendarImpl(exptime);

		loginTicketRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<loginTicketRequest version=\"1.0\">" + "<header>" + "<source>" + signerDN + "</source>"
				+ "<destination>" + dstDN + "</destination>" + "<uniqueId>" + UniqueId + "</uniqueId>"
				+ "<generationTime>" + XMLGenTime + "</generationTime>" + "<expirationTime>" + XMLExpTime
				+ "</expirationTime>" + "</header>" + "<service>" + service + "</service>" + "</loginTicketRequest>";

		// System.out.println("TRA: " + loginTicketRequestXml);
		return (loginTicketRequestXml);
	}
}
