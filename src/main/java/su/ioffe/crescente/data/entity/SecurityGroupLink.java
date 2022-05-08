package su.ioffe.crescente.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serializable;

/**
 * Associates user name with security group - we don't use any many-many relationships here to keep things simple.
 * See Group(s) in Duke’s Forest Case Study Example from JakartaEE tutorial for a many-to-many example.
 */
@Entity

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"groupName", "userAccountName"}))
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
