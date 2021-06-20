package com.hspan.hspan.controllers;

import com.hspan.hspan.data.models.User;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.UserBasicModel;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.dto.out.QUser;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Comparator;

@Transactional
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    Auth auth;

    @Autowired
    Snowflake snowflake;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("register")
    public QResponse createUser(@RequestBody UserBasicModel model) {
        var userInDb = userRepository.findByUsername(model.username);
        if (userInDb != null) {
            return new QResponse(null, -1L, "用户名已存在", false);
        }

//        var initialAvatarUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597064723297&di=c4d1baacccfaa045cbec77dfe4b8eacd&imgtype=0&src=http%3A%2F%2Fwww.cxyclub.cn%2FUpload%2FImages%2F2012022009%2F8466A231F7D5FFB3.jpg";

        var user = new User(snowflake.nextId(), model.username, model.password);
        userRepository.save(user);

        return new QResponse(null, user.getId(), "ok", true);
    }

    @GetMapping("{id}")
    public QResponse getUser(@PathVariable  Long id) {
        if (!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return QResponse.notFoundResponse("User");

        }
        return new QResponse(QUser.convert(user, modelMapper), user.getId(), "ok", true);
    }

    @GetMapping("me")
    public QResponse getMe() {
        if (!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }

        var user = userRepository.findById(auth.userId()).orElse(null);
        if (user == null) {
            return QResponse.notFoundResponse("User");
        }
        user.getFiles().sort(Comparator.naturalOrder());
        var res = QUser.convert(user, modelMapper);
        System.out.println(res);
        return new QResponse(res, res.id, "ok",true);
    }

    @PutMapping()
    public QResponse updateUser(@RequestBody UserBasicModel model) {
        if (!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }

        var id = auth.userId();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return QResponse.notFoundResponse("User");
        }

        user.setUsername(model.username);
        user.setPassword(model.password);

        userRepository.save(user);
        return new QResponse(null, id, "ok", true);
    }

}
