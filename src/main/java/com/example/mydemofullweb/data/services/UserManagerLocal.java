package com.example.mydemofullweb.data.services;

import com.example.mydemofullweb.data.entity.UserAccount;
import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import com.example.mydemofullweb.data.info.UserAccountSummary;
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
}
