package com.example.mydemofullweb.data.services;

import com.example.mydemofullweb.data.exceptions.UserNotFoundException;
import com.example.mydemofullweb.data.info.UserAccountSummary;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public interface UserService {
    @POST
    @Produces(APPLICATION_JSON)
    @Path("createOrUpdate")
    UserAccountSummary createOrUpdate(@FormParam("name") String name, @FormParam("fullName") String fullName,
                                      @FormParam("email") String email, @FormParam("colour") String colour,
                                      @FormParam("pictureURL") String pictureURL);

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("delete")
    void delete(@QueryParam("name") String userName) throws UserNotFoundException;

    @GET
    @Produces(APPLICATION_JSON)
    @Path("name/{name}")
    UserAccountSummary findByName(@PathParam("name") String name);

    @GET
    @Produces(APPLICATION_JSON)
    @Path("listAll")
    List<UserAccountSummary> findAll();

}
