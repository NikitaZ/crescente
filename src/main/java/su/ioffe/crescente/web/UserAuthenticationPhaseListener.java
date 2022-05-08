package su.ioffe.crescente.web;


import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nikita.zinoviev@gmail.com
 */
public class UserAuthenticationPhaseListener implements PhaseListener {

    private static final Logger LOGGER = Logger.getLogger(UserAuthenticationPhaseListener.class.getName());

    @Inject
    @Named("userBean")
    private UserBean userBean;


    @Inject
    @Named("ServiceBean")
    private ServiceBean serviceBean;

    @Inject
    private SecurityContext securityContext;

    public void afterPhase(PhaseEvent phaseEvent) {
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        if (!userBean.getLoggedIn()) {
            try {

                final FacesContext facesContext = phaseEvent.getFacesContext();
                final ExternalContext externalContext = facesContext.getExternalContext();
                final String requestContextPath = externalContext.getRequestPathInfo();
//                old logic which allowed to work with login or UserEdit page while logging in or registering
                if(requestContextPath != null) // todo this happens if the path to welcome file in web.xml doesn't contain /faces/
                if (!requestContextPath.contains("/Login.xhtml") && !requestContextPath.contains("/UserEdit.xhtml")) {
                    LOGGER.info("UserAuthenticationPhaseListener forwarding to login page");
                    externalContext.invalidateSession();
                    externalContext.redirect(externalContext.getApplicationContextPath() + "/faces/Login.xhtml");
                }

//                final FacesContext fctx = FacesContext.getCurrentInstance();
//                ExternalContext ctx = fctx.getExternalContext();

//                if (securityContext.getCallerPrincipal() == null) {
//                    // require login by redirecting to a secured page
//                    // Wow! this works so that when user visits our application while not being logged in
//                    // even though the welcome page is unsecure, he/she is forwarded to the Google login right away
//                    // without having to click the "please, log in" link.
//                    LOGGER.info("UserAuthenticationPhaseListener forwarding to login page");
//                    externalContext.invalidateSession();
//                    externalContext.redirect(externalContext.getApplicationContextPath() + "/faces/Login.xhtml");
//                } else
//                {
//                    // auto login logic
//                    final String principalName = securityContext.getCallerPrincipal().getName();
//
////                    if (!principalName.endsWith("@cm.games")) {
////                        // google suggests to have these checks enen though pur app.key doesn't allow other domains
////                        // see https://developers.google.com/identity/work/it-apps
////                        // and
////                        // https://developers.google.com/identity/protocols/oauth2/openid-connect#hd-param
////                        LOGGER.log(Level.SEVERE, "Alien user with e-mail " + principalName + " detected, should never happen, logging out");
////                        externalContext.invalidateSession();
////                        ((HttpServletRequest) externalContext.getRequest()).logout();
////                    } else
////
//                    {
//
//                        // requestContextPath may be null if the current URL (say path to welcome file in web.xml) doesn't contain /faces/
//                        // do not redirect if a user is already being registered
//                        if (requestContextPath == null || !requestContextPath.contains("/UserEdit.xhtml")) {
//                            LOGGER.info("Authenticated as " + principalName);
//                            UserAccountSummary userAccount = serviceBean.getUserService().findByName(principalName);
//                            if (userAccount != null) {
//                                userBean.loginUser(userAccount);
//                                LOGGER.info("Auto logged in as " + principalName);
//                            } else {
//                                LOGGER.info("Registering new user for " + principalName);
//                                externalContext.redirect(externalContext.getApplicationContextPath()
//                                        + "/faces/UserEdit.xhtml?logIntoOnSave=true&userName=" + principalName);
//                                // try to do it in a more elegant way, I wasn't able to make this work, apparently something is not initialized properly yet
//                                // empty view context leads to NPE at
////                                at com.sun.faces.application.view.MultiViewHandler.getRedirectURL(MultiViewHandler.java:390)
////                                at jakarta.faces.application.ViewHandlerWrapper.getRedirectURL(ViewHandlerWrapper.java:287)
////                                at org.jboss.weld.module.jsf.ConversationAwareViewHandler.getRedirectURL(ConversationAwareViewHandler.java:145)
//
//                                // this doesn't work due to "<if>#{not userBean.loggedIn}</if><to-view-id>/Login.xhtml</to-view-id>" rule
////                                facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null,
////                                        "UserEdit?faces-redirect=true&logIntoOnSave=true&userName=" + principalName);
//                                // see faces-config.xml for the Newcommer trick
////                                final Instance<LoginPageBean> select = CDI.current().select(LoginPageBean.class);
////                                final LoginPageBean loginPageBean = select.iterator().next();
////                                loginPageBean.setUserName(principalName);
////                                facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null,
////                                        "Newcommer");
//                            }
//                        }
//                    }
//                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Not cool...", e);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
