package com.company.models;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

@CsvDataType(defaultPrefix = "US")
public class User {

    @CsvField(pos = 1)
    private String username;

    @CsvField(pos = 2)
    private String userGroup;

    public User() {
    }

    public User(String username, String user_group) {
        this.username = username;
        this.userGroup = user_group;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String toString() {
        return "User: " + this.username + ", group:" + this.userGroup;
    }
}
