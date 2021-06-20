package com.hspan.hspan.data.models;

import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User extends RootEntity {

    private String username;

    private String password;

    @ElementCollection(targetClass=Long.class)
    private List<Long> files;

    public User() {

    }


    public User(long id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
        this.files = new ArrayList<>();
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void addFile(Long fileId) {
        this.files.add(fileId);
    }

    public void removeFile(Long fileId) {
        this.files.remove(fileId);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", files=" + files +
                '}';
    }
}
