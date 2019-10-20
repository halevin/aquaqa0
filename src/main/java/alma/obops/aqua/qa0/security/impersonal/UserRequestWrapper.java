package alma.obops.aqua.qa0.security.impersonal;

/**
 * Created by achalevi on 7/20/2016.
 */
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * An extension for the HTTPServletRequest that overrides the getUserPrincipal() and isUserInRole().
 *  We supply these implementations here, where they are not normally populated unless we are going through
 *  the facility provided by the container.
 * <p>If he user or roles are null on this wrapper, the parent request is consulted to try to fetch what ever the container has set for us.
 * This is intended to be created and used by the UserRoleFilter.
 * @author thein
 *
 */
public class UserRequestWrapper extends HttpServletRequestWrapper {

    protected Logger logger = Logger.getLogger( this.getClass().getCanonicalName() );

    String user;
    HttpServletRequest realRequest;

    public UserRequestWrapper(String user, HttpServletRequest request) {
        super(request);
        this.user = user;
        this.realRequest = request;
    }

    @Override
    public Principal getUserPrincipal() {
        if (this.user == null) {
            return realRequest.getUserPrincipal();
        }

        return new Principal() {
            @Override
            public String getName() {
                return user;
            }
        };
    }
}

