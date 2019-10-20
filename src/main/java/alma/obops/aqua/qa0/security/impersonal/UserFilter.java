package alma.obops.aqua.qa0.security.impersonal;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alma.obops.aqua.qa0.SnooPIConstants;
import alma.obops.boot.security.Role;
import alma.obops.boot.security.User;
import alma.obops.boot.security.UserDao;

@Component
public class UserFilter implements Filter {

    @Autowired
    private UserDao userDAO;

    protected Logger logger = Logger.getLogger( this.getClass().getCanonicalName() );

    public void init(FilterConfig cfg) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain next) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String newUser = request.getParameter(SnooPIConstants.USER_PARAMETER);
        boolean hasPermissionToImpersonate=false;

        Principal p = request.getUserPrincipal();
        if (p!=null) {
            String accountID = p.getName();
            User realUser = userDAO.findById(accountID).orElse(null);
            if (realUser != null) {
                for (Role role : realUser.getRoles()) {
                    if (SnooPIConstants.ARCA_ROLE.equals(role.getName())) {
                        hasPermissionToImpersonate = true;
                    }
                }
            }
        }

        if (newUser!=null&&newUser.length()>0){
            if (hasPermissionToImpersonate){
                logger.info("Impersonating for " + newUser);
                next.doFilter(new UserRequestWrapper(newUser, request), response);
                return;
            } else {
                logger.info("No permission to impersonate for " + newUser);
            }
        }
        next.doFilter(new UserRequestWrapper(null, request), response);

    }

    public void destroy() {
    }
}