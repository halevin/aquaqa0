package alma.obops.aqua.qa0.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Provides the URLs we need to interface with the rest of the system
 * @author amchavan
 */
public interface UrlConfiguration {

	/** @return The URL of the CAS server securing this application */
    String getCasUrl();

	/** @return This application's own URL */
    String getPublicProtrackRestServerUrl();
	
	/**
	 * @return The PT (staff project tracker) rest server url
	 */
    String getProtrackRestServerUrl();

	String getKayakoServerUrl();

	String getKayakoServerApiKey();

	String getKayakoServerSecretKey();
	
	int getMaxASDMReadingThreads();
}