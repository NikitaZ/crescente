package com.example.mydemofullweb.data.controllers;

import com.example.mydemofullweb.data.entity.UserAccount;
import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import jakarta.ejb.Local;

import java.util.List;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Local
public interface UserControllerLocal {
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
}
