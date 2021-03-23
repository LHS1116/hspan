package com.hspan.hspan.data.models;

import com.hspan.hspan.data.RootEntity;
import lombok.Getter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User extends RootEntity {

    private String username;

    private String password;

    private List<Long> files;


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
}
