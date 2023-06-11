package su.ioffe.crescente.data.db.entities;


import su.ioffe.crescente.data.utils.DateUtils;

import java.util.Date;

public class Child {

    private Date birthdate;
    private long id;
    private String nickname;
    private Boolean diagnoz;

    public Date getBirthdate() {
        return birthdate;
    }

    public static Child parseCSV(String line) {

//format: ID,Nickname,Birthdate,diagnoz

        Child child = new Child();
        String[] fields = line.split(",");
        child.id = Long.parseLong(fields[0].trim());
        child.nickname = fields[1];
        child.birthdate = DateUtils.stringToDate(fields[2]);
        child.diagnoz = null;
        if (fields.length >= 4 && fields[3] != null) {
            fields[3] = fields[3].trim();
            if ("1".equals(fields[3])) {
                child.diagnoz = true;
            } else if ("0".equals(fields[3])) {
                child.diagnoz = false;
            }
        }
        return child;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean isDiagnoz() {
        return diagnoz;
    }

    public void setDiagnoz(Boolean diagnoz) {
        this.diagnoz = diagnoz;
    }
}
