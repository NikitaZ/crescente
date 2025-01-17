package su.ioffe.crescente.data.info;

import su.ioffe.crescente.data.entity.NamedEntity;

import java.io.Serial;
import java.io.Serializable;

public class UserAccountSummary implements Serializable, NamedEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String fullName;

    private String email;

    private String colour;

    private String pictureURL;

    private static final String DEFAULT_USER_NAME = "Anonymous";

    public static final UserAccountSummary DEFAULT_USER = new UserAccountSummary(DEFAULT_USER_NAME, DEFAULT_USER_NAME, "", "", "");

    public UserAccountSummary() {
    }

    public UserAccountSummary(String name, String fullName, String email, String colour, String pictureURL) {
        this.name = name;
        this.fullName = fullName;
        this.email = email;
        this.colour = colour;
        this.pictureURL = pictureURL;
    }

    // todo for sure get rid of 'get' as this is info object(?) or may it cause problems for JSF?
    // upgrade JSF to newest version first
//    @Override
    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getColour() {
        return colour;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    @Override
    public String toString() {
        return name;
    }
}
