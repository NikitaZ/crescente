package com.example.mydemofullweb.web;


import com.example.mydemofullweb.data.info.UserAccountSummary;
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

import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

//import jakarta.annotation.ManagedBean;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("loginPageBean")
@RequestScoped
//@ManagedBean("loginPageBean") // does not work instead of @Named at least in case of JSF version="2.2" in faces-config.xml
public class LoginPageBean implements Serializable {

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
        Credential credential = new UsernamePasswordCredential(getUserName(), new Password(getPassword()));
        AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                withParams().credential(credential));

        if (status.equals(SEND_CONTINUE)) {
            facesContext.responseComplete();
        } else if (status.equals(SEND_FAILURE)) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Authentication failed", null));
        }

        // Now lets set some information about the user for out app.
        // We could have LDAP or OpenID authentication above, in which case a new user could come.
        UserAccountSummary userAccount = serviceBean.getUserService().findByName(getUserName());

        if (userAccount != null) {
            userBean.loginUser(userAccount);
            return "Users";
        } else {
            return "Newcommer";
        }
    }


}