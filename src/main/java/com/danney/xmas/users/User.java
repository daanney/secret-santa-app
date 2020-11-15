package com.danney.xmas.users;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String userKey;
    private Boolean sawAssigned;
    private String name;
    private String password;
    private Integer assignedUserId;
    private String token;

    @Transient
    private User assignedUser;

    public User() {
    }

    public User(Integer id, Integer assignedUserId) {
        this.id = id;
        this.assignedUserId = assignedUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Boolean getSawAssigned() {
        return sawAssigned;
    }

    public void setSawAssigned(Boolean sawAssigned) {
        this.sawAssigned = sawAssigned;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Integer assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public User getAssignedUser() {
        return assignedUser;
    }
}
