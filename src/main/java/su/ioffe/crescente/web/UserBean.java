package su.ioffe.crescente.web;


import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

/**
 * User: nikita.zinoviev@gmail.com
 */
@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String fullName;
    private String email;

    @Inject
    private HttpServletRequest request;

    public UserBean() {
        userName = null;
        fullName = null;
        email = null;
    }

//    public UserBean(String userName, String fullName, String email) {
//        this.userName = userName;
//        this.fullName = fullName;
//        this.email = email;
//    }

    @PostConstruct
    public void init() {

    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean getLoggedIn() {
        return userName != null;
    }

    public String logout() throws ServletException {
        userName = null;
        request.logout(); // invalidate google openid auth. (cf JSF and Java EE 8 book)
        request.getSession().invalidate();
        return "logout";
    }

    public void loginUser(UserAccountSummary userAccount) {
        this.userName = userAccount.getName();
        this.fullName = userAccount.getFullName();
        this.email = userAccount.getEmail();
    }
}