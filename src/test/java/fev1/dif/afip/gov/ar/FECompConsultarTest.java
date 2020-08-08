package fev1.dif.afip.gov.ar;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author janusky@gmail.com
 * @version 1.0 - 6 ago. 2020 17:21:46
 *
 */
public class FECompConsultarTest {

	static final String token = "COMPLETAR";
	static final String sign = "COMPLETAR";
	static final long cuit = 0L;

	static ObjectFactory objectFactory = new ObjectFactory();

	public static void main(String args[]) {
		Service service = new Service();

		ServiceSoap soap = service.getServiceSoap();

		FEAuthRequest auth = new FEAuthRequest();
		auth.setToken(token);
		auth.setSign(sign);
		auth.setCuit(cuit);
		FECompConsultaReq feCompConsReq = new FECompConsultaReq();
		int ptoVta = 1;
		int cbteTipo = 1;
		long cbteNro = 1l;
		feCompConsReq.setCbteNro(cbteNro);
		feCompConsReq.setCbteTipo(cbteTipo);
		feCompConsReq.setPtoVta(ptoVta);

		FECompConsultaResponse response = soap.feCompConsultar(auth, feCompConsReq);

		try {
			FECompConsultarResponse root = objectFactory.createFECompConsultarResponse();
			root.setFECompConsultarResult(response);

			StringWriter xml = printResponse(root);

			System.out.println(xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static StringWriter printResponse(Object response) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(response.getClass());
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(response, sw);
		return sw;
	}
}
