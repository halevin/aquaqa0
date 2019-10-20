package alma.obops.aqua.qa0.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * No-op, keep just in case
 * @author amchavan, 19-Aug-2014
 */

@Component
public class CookieProcessingFilter implements Filter {
    
	@Override
    public void destroy() {
    	// no-op
    }
    
    @SuppressWarnings("unused")
	@Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
    		throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        // no-op
		
		chain.doFilter( req, res );		
    }
    
    @Override
    public void init(FilterConfig ignored) throws ServletException {
    	// no-op
    }
}
