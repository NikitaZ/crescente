package su.ioffe.crescente.data.controllers;

import su.ioffe.crescente.data.entity.UserAccount;
import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import jakarta.ejb.Local;

import java.util.Collection;
import java.util.List;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Local
public interface UserControllerLocal {
    // todo should return Optional<UserAccount>
    UserAccount findByName(String name);

    /**
     * Looks for user with a given name
     *
     * @param name of the user to search for
     * @return Does not return null but throws exception
     * @throws UserNotFoundException if user with a given name cannot be found
     */
    UserAccount checkedFindByName(String name) throws UserNotFoundException;

    UserAccount create(String name);

    void delete(String userName) throws UserNotFoundException;

    List<UserAccount> findAll();

    void revokeRights(String name);

    void addToSecurityRole(String userName, String role);

    Collection<String> findUserRoles(String userName);

    void changePassword(String userName, String newPassword) throws UserNotFoundException;
}
