package com.hspan.hspan.controllers;

import com.hspan.hspan.data.models.User;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.UserBasicModel;
import com.hspan.hspan.dto.out.IdDto;
import com.hspan.hspan.dto.out.QUser;
import com.hspan.hspan.exception.BadRequestException;
import com.hspan.hspan.exception.NotFoundException;
import com.hspan.hspan.exception.UnauthorizedException;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.modelmapper.ModelMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    Auth auth;

    @Autowired
    Snowflake snowflake;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public IdDto createUser(@RequestBody UserBasicModel model) {
        var userInDb = userRepository.findByUsername(model.username);
        if (userInDb != null) {
            throw new BadRequestException("Username already exist");
        }

//        var initialAvatarUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597064723297&di=c4d1baacccfaa045cbec77dfe4b8eacd&imgtype=0&src=http%3A%2F%2Fwww.cxyclub.cn%2FUpload%2FImages%2F2012022009%2F8466A231F7D5FFB3.jpg";

        var user = new User(snowflake.nextId(), model.username, model.password);
        userRepository.save(user);

        return new IdDto(user.getId());
    }

    @GetMapping("{id}")
    public QUser getUser(@PathVariable  Long id) {
        if (!auth.isLoggedIn()) {
            throw new UnauthorizedException();
        }
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException();
        }
        return QUser.convert(user, modelMapper);
    }

    @PutMapping()
    public IdDto updateUser(@RequestBody UserBasicModel model) {
        if (!auth.isLoggedIn()) {
            throw new UnauthorizedException();
        }

        var id = auth.userId();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException();
        }

        user.setUsername(model.username);
        user.setPassword(model.password);

        userRepository.save(user);
        return new IdDto(id);
    }

}
