package com.hspan.hspan.controllers;

import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.UserBasicModel;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;


@Transactional
@RestController
@RequestMapping("/api/login/")
public class LoginController {
    @Autowired
    Auth auth;

    @Autowired
    Snowflake snowflake;

    @Autowired
    UserRepository userRepository;

    @GetMapping("test")
    @ResponseBody
    public String test() {
        System.out.println("test");
        return "Login Test!";
    }

    @PostMapping("in")
    @ResponseBody
    public QResponse login(@RequestBody UserBasicModel info, HttpSession session) {
        var user = userRepository.findByUsername(info.username);
        System.out.println(info.password + info.username);
        if(user == null) {
            return QResponse.notFoundResponse("username");


        }
        if (!user.checkPassword(info.password)) {
            return new QResponse("密码错误", -1L, "200", false);

        }
        session.setAttribute("userID", user.getId());
        session.setAttribute("username", user.getUsername());
        return new QResponse("登录成功！", user.getId(), "200", true);
    }

    @GetMapping("out")
    @ResponseBody
    public QResponse logout(HttpSession session) {
        if (session.getAttribute("userID") == null || session.getAttribute("username") == null) {
            return new QResponse("注销成功！", -1L, "200", true);
        }
        Long id = (Long)session.getAttribute("userID");

        session.removeAttribute("userID");
        session.removeAttribute("username");
        return new QResponse("注销成功！", id, "200", true);

    }

}
