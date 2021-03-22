package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.User;
import org.modelmapper.ModelMapper;

import java.util.List;

public class QUser {
    private String username;
    private Long id;
    private List<QHFile> files;

    public QUser(String username, Long id, List<QHFile> files) {
        this.username = username;
        this.id = id;
        this.files = files;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public List<QHFile> getFiles() {
        return files;
    }



    public static QUser convert(User user, ModelMapper mapper) {
        if (user == null) {
            return null;
        }
        return mapper.map(user, QUser.class);
    }
}
