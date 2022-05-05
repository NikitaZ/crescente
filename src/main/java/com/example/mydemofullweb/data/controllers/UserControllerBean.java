package com.example.mydemofullweb.data.controllers;

import com.example.mydemofullweb.data.entity.UserAccount;

import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Stateless
public class UserControllerBean implements UserControllerLocal {

    @PersistenceContext(name = "adminDB")
    private EntityManager em;

    public UserAccount findByName(String name) {

        List<UserAccount> resultList = em.createNamedQuery(UserAccount.FIND_BY_NAME, UserAccount.class).setParameter(UserAccount.PARAM_NAME, name).getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        if (resultList.size() > 1) {
            throw new EJBException(" DB integrity violation, looked up more than one UserAccount for name " + name);
        }

        return resultList.get(0);
    }


    public List<UserAccount> findAll() {
        return em.createNamedQuery(UserAccount.FIND_ALL, UserAccount.class).getResultList();
    }


    /**
     * Looks for user with a given name
     *
     * @param name of the user to search for
     * @return Does not return null but throws exception
     * @throws UserNotFoundException if user with a given name cannot be found
     */
    public UserAccount checkedFindByName(String name) throws UserNotFoundException {
        UserAccount user = findByName(name);

        if (user == null) {
            throw new UserNotFoundException(name);
        }

        return user;
    }


    public UserAccount create(String name) {
        UserAccount user = new UserAccount(name);
        em.persist(user);
        return user;
    }

    /**
     * Deletes user with given username if one exists
     *
     * @param userName to delete
     * @throws UserNotFoundException
     */
    public void delete(String userName) throws UserNotFoundException {
        UserAccount user = findByName(userName);

        if (user == null) {
            throw new UserNotFoundException(userName);
        }

        em.remove(user);
    }

}