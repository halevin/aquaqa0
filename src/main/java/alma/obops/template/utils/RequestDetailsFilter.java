/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/

package alma.obops.template.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
 * Debugging tool: logs all requests on stdout, including headers, etc..
 * @author amchavan, 19-Aug-2014
 */

@Component
public class RequestDetailsFilter implements Filter {
    
	private static final boolean DO_LOG = false;

	private static final Logger logger = Logger.getLogger(RequestDetailsFilter.class.getSimpleName());

	@Override
    public void destroy() {
    	// no-op
    }
    
    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
    		throws IOException, ServletException {

		if( DO_LOG ) {
			HttpServletRequest httpReq = (HttpServletRequest) req;

			String uri = httpReq.getRequestURI();
			Map<String, String[]> parameters = httpReq.getParameterMap();
			Enumeration<String> headerNames = httpReq.getHeaderNames();
			Cookie[] cookies = httpReq.getCookies();
			logger.info(">>> URI: " + uri + ",\n    parameters: ["
					+ parameterMapToString(parameters) + "]"
					+ ",\n    headers: ["
					+ headersEnumToString(headerNames, httpReq) + "]"
					+ ",\n    cookies: [" + cookiesToString(cookies) + "]");
		}
    	
//		if (httpReq.getMethod().equalsIgnoreCase("POST")) {
//
//			MyRequestWrapper myRequestWrapper = new MyRequestWrapper(
//					(HttpServletRequest) req);
//			String body = myRequestWrapper.getBody();
//			System.out.println(body);
//		}
		 
		// move on to the next filter, no change to either req or res
		chain.doFilter( req, res );		
    }
    
    @Override
    public void init(FilterConfig ignored) throws ServletException {
    	// no-op
    }
    
    // utility method
	private static String headersEnumToString(Enumeration<String> headerNames, HttpServletRequest request) {
    	StringBuilder sb = new StringBuilder();
		while( headerNames.hasMoreElements() ) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			if( sb.length() != 0 ) {
				sb.append( ", " );
			}
			sb.append( "\n\t" ).append( headerName ).append(": ").append( headerValue );
		}
		return sb.toString();
	}

	// utility method
	private static String parameterMapToString( Map<String, String[]> map ) {
    	StringBuilder sb = new StringBuilder();
    	for (String key : map.keySet()) {
			String value = map.get(key)[0];
			if( sb.length() != 0 ) {
				sb.append( ", " );
			}
			sb.append( key ).append('=').append(value);
		}
    	return sb.toString();
    }
	
    // utility method
	private static String cookiesToString( Cookie[] cookies ) {
		if( cookies == null ) {
			return "[none]";
		}
		
    	StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if( sb.length() != 0 ) {
				sb.append( ", " );
			}
			sb.append( "\n\t" ).append( cookie.getName() ).append(": ").append( cookie.getValue() );
		}
		return sb.toString();
	}
}

/*
class MyRequestWrapper extends HttpServletRequestWrapper {
	 private final String body;
	 public MyRequestWrapper(HttpServletRequest request) throws IOException {
	   super(request);
	   StringBuilder stringBuilder = new StringBuilder();
	   BufferedReader bufferedReader = null;
	   try {
	     InputStream inputStream = request.getInputStream();
	     if (inputStream != null) {
	       bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	       char[] charBuffer = new char[128];
	       int bytesRead = -1;
	       while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	         stringBuilder.append(charBuffer, 0, bytesRead);
	       }
	     } else {
	       stringBuilder.append("");
	     }
	   } catch (IOException ex) {
	       throw ex;
	   } finally {
	     if (bufferedReader != null) {
	       try {
	         bufferedReader.close();
	       } catch (IOException ex) {
	         throw ex;
	       }
	     }
	   }
	   body = stringBuilder.toString();
	 }

	 @Override
	 public ServletInputStream getInputStream() throws IOException {
	   final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
	   ServletInputStream servletInputStream = new ServletInputStream() {
	     public int read() throws IOException {
	       return byteArrayInputStream.read();
	     }
	   };
	   return servletInputStream;
	 }

	 @Override
	 public BufferedReader getReader() throws IOException {
	   return new BufferedReader(new InputStreamReader(this.getInputStream()));
	 }

	 public String getBody() {
	   return this.body;
	 }
	}
*/
