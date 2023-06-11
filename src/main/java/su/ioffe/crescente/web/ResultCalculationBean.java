package su.ioffe.crescente.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import su.ioffe.crescente.data.info.UserAccountSummary;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("userEditPageBean")
@RequestScoped
public class ResultCalculationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient String userID;

    private transient String birthday;

    private transient String analysisDate;

    private transient String lg;

    private transient String fsg;

    private transient String tst;

    private transient String ing;

    @Inject
    @Named("ServiceBean")
    private transient ServiceBean serviceBean;

    @Inject
    @Named("userBean")
    private transient UserBean userBean;

    @Inject
    private transient SecurityContext securityContext;
//
//    @Inject
//    private transient OpenIdContext openIdContext;

    public ResultCalculationBean() {  }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(String analysisDate) { this.analysisDate = analysisDate; }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
    }

    public String getFsg() {
        return fsg;
    }

    public void setFsg(String fsg) {
        this.fsg = fsg;
    }

    public String getTst() {
        return tst;
    }

    public void setTst(String tst) {
        this.tst = tst;
    }
    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    /*
     * JSF action methods, return null to refresh the page
     */
    public String calc() {



        if (securityContext.isCallerInRole("admin") || isReallyEditingOwnProfile()) {
            UserAccountSummary user = serviceBean.getUserService().createOrUpdate(getUserName(), getFullName(), getEmail(), getColour(), getPictureURL());
            if (logIntoOnSave) {
                userBean.loginUser(user);
                return "About";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are not allowed to modify profiles of other users.", "Error during edit of user '" + userName + "'"));
        }
        if (securityContext.isCallerInRole("admin")) {
            return "Users";
        }
        else {
            return "About";
        }
    }

    public void revokeRights() {
        serviceBean.getUserService().revokeRights(getUserName());
    }

    public void addToRole(String role) {
        serviceBean.getUserService().addToSecurityRole(getUserName(), role);
    }

    public void changePassword() {
        if (!isReallyEditingOwnProfile()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are not allowed to change passwords of other users.", "Error during edit of user '" + userName + "'"));
        } else  if (!Objects.equals(getPassword(), getRepeatedPassword())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The new passwords do not match.", ""));
        }
        else if (getPassword() == null || getPassword().isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The new password cannot be empty or blank.", ""));
        } else {
            try {
                serviceBean.getUserService().changePassword(getUserName(), getPassword().trim());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "The password has been changed!", ""));
            } catch (UserNotFoundException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Unable to change password for not yet saved new user.", ""));
            }
        }
    }

    public void resetPassword() {
        if (securityContext.isCallerInRole("admin")) {
            try {
                // reset password to user name
                serviceBean.getUserService().changePassword(getUserName(), getUserName());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "The password has been reset!", ""));
            } catch (UserNotFoundException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Unable to reset password for the user.", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are not allowed to reset passwords of other users.", "Error during edit of user '" + userName + "'"));
        }
    }

}