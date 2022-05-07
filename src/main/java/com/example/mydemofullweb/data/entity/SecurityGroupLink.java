/*
 * Permission to use, copy, modify, and/or distribute this software for any 
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR(S) DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION
 * OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.example.mydemofullweb.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

/**
 * Associates user name with security group - we don't use any many-many relationships here to keep things simple.
 * See Group(s) in Duke’s Forest Case Study Example from JakartaEE tutorial for a many-to-many example.
 */
@Entity
public class SecurityGroupLink implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    private String groupName;
    
    /**
     * Stores the username.
     */
    private String userAccountName;

    public Integer getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserAccountName() {
        return userAccountName;
    }
    
    /**
     * Set the name.
     * 
     * @param name the name.
     */
    public void setGroupName(String name) {
        this.groupName = name;
    }
    
    /**
     * Set the username.
     * 
     * @param username the username.
     */
    public void setUserAccountName(String username) {
        this.userAccountName = username;
    }
}
