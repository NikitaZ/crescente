package com.example.mydemofullweb.web;

import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import com.example.mydemofullweb.data.info.UserAccountSummary;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import java.io.Serializable;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("userEditPageBean")
@RequestScoped
public class UserEditPageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient String userName;

    private transient String fullName;

    private transient String email;

    private transient String colour;

    private transient String pictureURL;

    private transient boolean logIntoOnSave;

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

    public UserEditPageBean() {
        setColour("#FFFFFF");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getUserNameViaParameter() {
        return null;
    }

    // this method is always called unless above getter returns null (then it is just not called at all)!
    public void setUserNameViaParameter(String name) {
        if (name == null) {
            return;
        }

        UserAccountSummary user = serviceBean.getUserService().findByName(name);
        if (user == null) {
            setUserName(name);
            if (isEditingOwnOpenIDProfile()) {
//                initializeFromOpenID();
            }
            return;
        }

        setUserName(user.getName());
        setFullName(user.getFullName());
        setEmail(user.getEmail());
        setColour(user.getColour());
        setPictureURL(user.getPictureURL());
    }

    public boolean isEditingOwnOpenIDProfile() {
        return false; //this.userName != null && this.userName.equals(securityContext.getCallerPrincipal().getName());
    }

//    private void initializeFromOpenID() {
//        OpenIdClaims claims = openIdContext.getClaims();
//        if(claims != null) {
//            setFullName(claims.getName());
//            setEmail(claims.getEmail());
//            setPictureURL(claims.getPicture());
//        }
//    }

    public boolean getLogIntoOnSave() {
        return logIntoOnSave;
    }

    public void setLogIntoOnSave(boolean logIntoOnSave) {
        this.logIntoOnSave = logIntoOnSave;
    }

    public boolean getReadOnly() {
        return false; // no access restrictions in this app
    }

    /*
     * JSF action methods, return null to refresh the page
     */
    public String delete() {
        try {
            serviceBean.getUserService().delete(getUserName());
        } catch (UserNotFoundException e) {
            // nice, it displays the error but on the Users page!
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Error during delete"));
        }
        return "Users";
    }

    public String save() {
        // TODO!! fix it, it almost works
        if (/*securityContext.isCallerInRole("admin")*/ true || isEditingOwnOpenIDProfile()) {
            UserAccountSummary user = serviceBean.getUserService().createOrUpdate(getUserName(), getFullName(), getEmail(), getColour(), getPictureURL());
            if (logIntoOnSave) {
                userBean.loginUser(user);
                return "Users";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "You are not allowed to modify profiles of other users.", "Error during edit of user '" + userName + "'"));
        }

        return "Users";
    }


}