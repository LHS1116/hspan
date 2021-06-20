package com.hspan.hspan.dto.out;

import com.hspan.hspan.data.models.User;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Setter
@Getter
public class QUser {
    public String username;
    public Long id;
    public List<Long> files;

    public QUser(String username, Long id, List<Long> files) {
        this.username = username;
        this.id = id;
        this.files = files;
    }

    public QUser() {}

    public static QUser convert(User user, ModelMapper mapper) {
        if (user == null) {
            return null;
        }

        return mapper.map(user, QUser.class);
    }

    @Override
    public String toString() {
        return "QUser{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", files=" + files +
                '}';
    }
}
