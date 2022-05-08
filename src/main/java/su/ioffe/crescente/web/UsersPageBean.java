package su.ioffe.crescente.web;


import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Named("usersPageBean")
@RequestScoped
public class UsersPageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient List<UserAccountSummary> users;

    @Inject
    @Named("ServiceBean")
    private transient ServiceBean serviceBean;

    public List<UserAccountSummary> getUsers() {
        if (users == null) {
            users = serviceBean.getUserService().findAll();
        }
        return users;
    }

    public String refresh() {
        return null;
    }

}