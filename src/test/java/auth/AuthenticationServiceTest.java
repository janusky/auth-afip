package auth;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author janusky@gmail.com
 * @version 1.0 - 6 ago. 2020 17:22:02
 *
 */
public class AuthenticationServiceTest {

	public static void main(String[] args) {
		try {
			Properties config = new Properties();
			config.load(new FileInputStream("./config/wsaa_client.properties"));

			AuthenticationService authenticationService = new AuthenticationService();

			LoginResponse loginResponse = authenticationService.login(config);

			System.out.println(loginResponse.getAsXML());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
