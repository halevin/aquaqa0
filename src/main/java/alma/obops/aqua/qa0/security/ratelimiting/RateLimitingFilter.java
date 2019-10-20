package alma.obops.aqua.qa0.security.ratelimiting;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

/**
 * Rate limiting filter,
 * see https://github.com/vladimir-bukhtoyarov/bucket4j/blob/master/doc-pages/basic-usage.md
 * @author amchavan
 * @author rkurowsk
 */
public class RateLimitingFilter implements Filter {

	private static final Logger logger = Logger.getLogger( RateLimitingFilter.class.getName() );

	private Bucket mainBucket;
	private Map<String, Bucket> limiters = new HashMap<>();
	private long maxReqPerSec;
	private long maxReqPerSecPerOrigin;

	/**
	 * Constructor
	 *
	 * @param maxReqPerSec
	 *            Maximum allowed number of concurrent requests per second; to
	 *            avoid that one client hogs the whole bandwidth, each client
	 *            (that is, originating IP address) will be allocated part of
	 *            that maximum
	 */
	public RateLimitingFilter(long maxReqPerSec, long maxReqPerSecPerOrigin ) {
		this.maxReqPerSec = maxReqPerSec;
		this.maxReqPerSecPerOrigin = maxReqPerSecPerOrigin;
		this.mainBucket = buildBucket( maxReqPerSec );
	}

	private Bucket buildBucket(long maxReqPerSec){

		Bandwidth limit = Bandwidth.simple( maxReqPerSec, Duration.ofSeconds(1));
		return Bucket4j.builder().addLimit( limit ).build();

	}

	@Override
	public void destroy() {
		// no-op
	}

	@Override
	public void doFilter( ServletRequest servletRequest,
						  ServletResponse servletResponse,
						  FilterChain filterChain ) throws IOException, ServletException {

		HttpServletRequest request  = (HttpServletRequest) servletRequest;

		// Check if this request should be served
		if( ! mainBucket.tryConsume( 1 )) {
			// limit is exceeded: return 429 Too Many Requests
			rejectRequest( servletResponse, maxReqPerSec, "Global");
			return;
		}

		// In principle we should let this request be served, but
		// first check if *this* *originator* should be served
		String originator = getOriginator( request );
		Bucket bucket = findBucket( originator );
		if( bucket.tryConsume( 1 )) {
			// the limit is not exceeded, continue processing the request
			filterChain.doFilter( servletRequest, servletResponse );
		}
		else {
			// limit is exceeded: return 429 Too Many Requests
			rejectRequest( servletResponse, maxReqPerSecPerOrigin, "Per origin" );
		}
	}

	// Get hold of a bucket for this originator
	private Bucket findBucket( String originator ) {
		Bucket bucket = limiters.get( originator );
		if( bucket == null ) {
			bucket = buildBucket( maxReqPerSecPerOrigin );
			limiters.put( originator, bucket );
		}
		return bucket;
	}

	private String getOriginator(HttpServletRequest request) {
		String originator = request.getHeader( "X-Forwarded-For" );		// See if the proxy has some info
		if( originator == null ) {
			originator = request.getRemoteHost();						// No info, get hostname from request
		}
		return originator;
	}

	@Override
	public void init( FilterConfig arg0 ) throws ServletException {
		// no-op
	}

	// Respond with a 429 TooManyRequests HTTP code
	private void rejectRequest(ServletResponse servletResponse, long maxReqPerSec, String rateType) throws IOException {

		String msg = rateType + " limit of " + maxReqPerSec + " requests per sec exceeded";

		logger.warning(msg);

		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setContentType("text/plain");
		response.setStatus(429);
		response.getWriter().append(msg);
	}
}
