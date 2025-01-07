package su.ioffe.crescente.web;


import jakarta.servlet.ServletException;
import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.security.enterprise.AuthenticationStatus.NOT_DONE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("loginPageBean")
@RequestScoped
public class LoginPageBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(LoginPageBean.class.getName());

    private static final long serialVersionUID = 1L;

    private transient String userName;

    private transient String password;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    @Inject
    @Named("ServiceBean")
    private transient ServiceBean serviceBean;

    @Inject
    @Named("userBean")
    private transient UserBean userBean;

    public LoginPageBean() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserNameViaParameter() {
        return null;
    }

    // this method is always called unless above getter returns null (then it is just not called at all)!
    public void setUserNameViaParameter(String name) {
        if (name == null) {
            return;
        }
        setUserName(name);
    }

    public boolean getReadOnly() {
        return false; // no access restrictions in this app
    }

    public String login() {
        if (securityContext.getCallerPrincipal() != null &&
                !securityContext.getCallerPrincipal().getName().equals(getUserName())) {
            externalContext.invalidateSession();
            try {
                ((HttpServletRequest) externalContext.getRequest()).logout();
            } catch (ServletException e) {
                LOGGER.log(Level.SEVERE, "Not cool...", e);
            }
            return null;
        }
        Credential credential = new UsernamePasswordCredential(getUserName(), new Password(getPassword()));
        AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                withParams().credential(credential));
//                withParams().credential(credential).newAuthentication(true));
        if (status.equals(NOT_DONE)) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Something is wrong with authentication configuration or password is not set, please contact system administrator.", null));
            // should never
            LOGGER.warning("Got NOT_DONE auth result, should never happen.");
            return null;
        } else if (status.equals(SUCCESS)) {
            // should never happen(?), unless it's newAuth. see comment above
            // well, it may happen if an already logged in user logs in.
            LOGGER.info("Got SUCCESS auth result.");
        } else if (status.equals(SEND_CONTINUE)) {
            facesContext.responseComplete();
        } else if (status.equals(SEND_FAILURE)) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Authentication failed", null));
            return null;
        } else {
            LOGGER.warning("Got " + status + "auth result, should never happen.");
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Something is wrong with authentication configuration, please contact system administrator.", null));
            return null;
        }

        // Now lets set some information about the user for out app.
        // We could have LDAP or OpenID authentication above, in which case a new user could come.
        UserAccountSummary userAccount = serviceBean.getUserService().findByName(getUserName());

        if (userAccount != null) {
            userBean.loginUser(userAccount);
            return "About";
        } else {
            return "Newcomer";
        }
    }


}