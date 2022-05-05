package com.example.mydemofullweb.data.services;

import com.example.mydemofullweb.data.entity.UserAccount;
import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import com.example.mydemofullweb.data.info.UserAccountSummary;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UserManagerLocal {
    // these methods are for EJB layer only

    // we need this for instance to build relationships, etc.
    UserAccount checkedFindUserByName(String name) throws UserNotFoundException;

    UserAccountSummary checkedFindUserSummaryByName(String name) throws UserNotFoundException;

    UserAccountSummary createOrUpdate(String name, String fullName, String email, String colour, String pictureURL);

    void delete(String userName) throws UserNotFoundException;

    UserAccountSummary findByName(String name);

    List<UserAccountSummary> findAll();
}
