package alma.obops.aqua.qa0.configuration;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import alma.obops.aqua.qa0.utils.SnooPIUtils;

/**
 * Implements the {@link UrlConfiguration} interface by exposing the properties
 * defined in <em>$ACSDATA/config/obopsConfig.properties</em>
 * 
 * @author amchavan, 22-Aug-2014
 */

@Component
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class ObopsConfiguration implements UrlConfiguration {

	@Autowired
	private Environment env;
	
	final static int DEFAULT_MAX_THREADS = 10;
	
	private final static Logger logger = Logger.getLogger(ObopsConfiguration.class.getName());
	

	@Override
	public String getCasUrl() {
		final String propName = "cas.url";
		final String url = env.getProperty( propName );
		
		SnooPIUtils.checkIfDefined( propName, url );
		
		return url;
	}

	@Override
	public int getMaxASDMReadingThreads() {
		final String propName = "obops.snoopi.maxasdmreadingthreads";
		final String url = env.getProperty( propName );
		
		try {
			return Integer.parseInt(url);
		} catch (Exception e){
			logger.warning("obops.snoopi.maxasdmreadingthreads is not defined. Using default "+DEFAULT_MAX_THREADS);
		}
		return DEFAULT_MAX_THREADS;
	}
	
	
	@Override
	public String getProtrackRestServerUrl() {
		
		final String propName = "obops.protrack.url";
		final String url = env.getProperty( propName );

		SnooPIUtils.checkIfDefined( propName, url );
		
		return url;
	}

	@Override
	public String getPublicProtrackRestServerUrl() {
		
		final String propName = "obops.public.protrack.rest.server.url";
		final String url = env.getProperty( propName );

		SnooPIUtils.checkIfDefined( propName, url );
		
		return url;
	}
	
/*
 * obops.kayako.server.url = https://sus-dev.nrao.edu/staging_alma_455_snoopy
obops.kayako.server.apiKey = 8cc3ce9a-ee7a-b294-f9a1-df45435e49dc
obops.kayako.server.secretKey = YjYyMGJmOGUtMjgxZS01M2I0LWVkOTktMGQzMzI3MTU2YmQ3MzExOGYyZTctYjdjZS00ZGE0LWY1NDYtNzBkODFjNTNkZmI0

(non-Javadoc)
 * @see alma.obops.aqua.qa0.UrlConfiguration#getProtrackRestServerUrl()
 */
	
	@Override
	public String getKayakoServerUrl() {
		
		final String propName = "obops.kayako.server.url";
		String url = env.getProperty( propName );
		SnooPIUtils.checkIfDefined( propName, url );
		
		// make sure we do not have any trailing slashes
		if( url.endsWith( "/" )) {
			url = url.substring( 0, url.length()-1 );
		}
		
		return url;
	}
	
	@Override
	public String getKayakoServerApiKey() {
		
		final String propName = "obops.kayako.server.apiKey";
		final String property = env.getProperty( propName );
		SnooPIUtils.checkIfDefined( propName, property );
		return property;
	}
	
	@Override
	public String getKayakoServerSecretKey() {
		
		final String propName = "obops.kayako.server.secretKey";
		final String property = env.getProperty( propName );
		SnooPIUtils.checkIfDefined( propName, property );
		return property;
	}
}

