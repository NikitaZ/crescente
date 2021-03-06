package su.ioffe.crescente.data.services;

import su.ioffe.crescente.data.controllers.UserControllerLocal;
import su.ioffe.crescente.data.entity.UserAccount;
import su.ioffe.crescente.data.exceptions.UserNotFoundException;
import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Stateless
public class UserManagerBean implements UserManagerLocal {

    @EJB
    private UserControllerLocal userController;

    private static final Logger LOGGER = Logger.getLogger(UserManagerBean.class.getName());

    private /*static*/ List<UserAccountSummary> convertToSummaryList(Collection<UserAccount> accounts) {
        return accounts.stream().map(UserAccount::toUserAccountSummary).collect(Collectors.toList());
    }

    @Override
    public List<UserAccountSummary> findAll() {
        return convertToSummaryList(userController.findAll());
    }

    @Override
    public UserAccountSummary findByName(String name) {
        return wrap(userController.findByName(name));
    }

    @Override
    public UserAccount checkedFindUserByName(String name) throws UserNotFoundException {
        return userController.checkedFindByName(name);
    }

    @Override
    public UserAccountSummary checkedFindUserSummaryByName(String name) throws UserNotFoundException {
        return checkedFindUserByName(name).toUserAccountSummary();
    }

    @Override
    public void revokeRights(String name) {
        userController.revokeRights(name);
    }

    @Override
    public void addToSecurityRole(String userName, String role) {
        userController.addToSecurityRole(userName, role);
    }

    @Override
    public Collection<String> findUserRoles(String userName) {
        return userController.findUserRoles(userName);
    }

    @Override
    public void changePassword(String userName, String newPassword) throws UserNotFoundException {
        userController.changePassword(userName, newPassword);
    }

    @Override
    public UserAccountSummary createOrUpdate(String name, String fullName, String email, String colour, String pictureURL) {
        LOGGER.log(Level.INFO, "createOrUpdateUser: {0} fullName: {1}, e-mail: {2}, colour: {3}, pictureURL: {4}",
                new Object[]{name, fullName, email, colour});

        UserAccount account = userController.findByName(name);
        if (account == null) {
            account = userController.create(name);
        }
        account.setFullName(fullName);
        account.setEmail(email);
        account.setColour(colour);
        account.setPictureURL(pictureURL);
        return account.toUserAccountSummary();
    }

    @Override
    public void delete(String userName) throws UserNotFoundException {
        userController.delete(userName);
    }

//    not used
//    @Override
//    public void deleteUser(String userName) {
//        userController.delete(userName);
//    }

    private /*static*/ UserAccountSummary wrap(UserAccount userAccount) {
        return userAccount != null ? userAccount.toUserAccountSummary() : null;
    }
}
