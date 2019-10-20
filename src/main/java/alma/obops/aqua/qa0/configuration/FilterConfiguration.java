package alma.obops.aqua.qa0.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import alma.obops.aqua.qa0.security.ratelimiting.RateLimitingFilter;
import alma.obops.aqua.qa0.utils.SnooPIUtils;
import alma.obops.boot.filters.RequestLoggingFilter;

@Configuration
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class FilterConfiguration {

	@Autowired
	Environment env;

	protected Logger logger = LogManager.getLogger( FilterConfiguration.class );

	/**
	 * @return A request logging filter
	 */
	@Bean
	public RequestLoggingFilter requestLoggingFilter() {
		return new RequestLoggingFilter();	
	}

	@Bean
	public RateLimitingFilter makeRateLimitingFilter() { return new RateLimitingFilter(getRateLimit(false), getRateLimit(true)); }


	public int getRateLimit(boolean perOrigin) {
		final String propName;
		if ( perOrigin ) {
			propName = "obops.snoopi.ratelimit.per.origin";
		} else {
			propName = "obops.snoopi.ratelimit";
		}
		final String limit = env.getProperty( propName );

		SnooPIUtils.checkIfDefined( propName, limit );

		int limitValue = Integer.MAX_VALUE;
		try {
			limitValue = Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			logger.warn("Cannot parse rate limit value");
		}

		return limitValue;
	}

}
