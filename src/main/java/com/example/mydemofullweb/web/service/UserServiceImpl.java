package com.example.mydemofullweb.web.service;

import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import com.example.mydemofullweb.data.info.UserAccountSummary;
import com.example.mydemofullweb.data.services.UserManagerLocal;
import com.example.mydemofullweb.data.services.UserService;
import jakarta.annotation.ManagedBean;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Path;

import java.util.List;

/**
 * Exports UserManagerBean through a JAX-RS service.
 */
@Path("/userService")
@ManagedBean
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