package su.ioffe.crescente.data.services;

import su.ioffe.crescente.data.entity.UserAccount;
import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.ejb.Local;

import java.util.Collection;

@Local
public interface UserManagerLocal extends UserService {
    // these methods are for EJB layer only

    // we need this for instance to build relationships, etc.
    UserAccount checkedFindUserByName(String name) throws UserNotFoundException;

    UserAccountSummary checkedFindUserSummaryByName(String name) throws UserNotFoundException;

    void revokeRights(String name);

    void addToSecurityRole(String userName, String role);

    Collection<String> findUserRoles(String userName);

    void changePassword(String userName, String newPassword) throws UserNotFoundException;
}
