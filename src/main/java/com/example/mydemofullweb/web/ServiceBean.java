package com.example.mydemofullweb.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 * Application scope data bean for the application.
 * An instance of this class is created automatically, the first
 * time application evaluates a value binding expression or method binding
 * expression that references a managed bean using this class.
 */
@Named("ServiceBean")
@ApplicationScoped
public class ServiceBean {

//     this is a bit more performant than
//    private UserService userManager = new UserServiceClient();
//    @EJB
//    private UserManagerLocal userManager;

    public boolean getDevelopment() {
        return FacesContext.getCurrentInstance().getApplication().getProjectStage() == ProjectStage.Development;
    }

    public String getAuthors() {
        return "Alexander Ioffe and Nikita Zinoviev";
    }
}