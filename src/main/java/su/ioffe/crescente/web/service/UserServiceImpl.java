package su.ioffe.crescente.web.service;

import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import su.ioffe.crescente.data.info.UserAccountSummary;
import su.ioffe.crescente.data.services.UserManagerLocal;
import su.ioffe.crescente.data.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;

import java.util.List;

/**
 * Exports UserManagerBean through a JAX-RS service.
 */
@Path("/userService")
public class UserServiceImpl implements UserService {

    @EJB
    private UserManagerLocal userManagerLocal;

    @Override
    public UserAccountSummary findByName(String name) {
        return userManagerLocal.findByName(name);
    }

    @Override
    public UserAccountSummary createOrUpdate(String name, String fullName, String email, String colour, String pictureURL) {
        return userManagerLocal.createOrUpdate(name, fullName, email, colour, pictureURL);
    }

    @Override
    public void delete(String userName) throws UserNotFoundException {
        userManagerLocal.delete(userName);
    }

    @Override
    public List<UserAccountSummary> findAll() {
        return userManagerLocal.findAll();
    }

}