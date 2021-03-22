package com.hspan.hspan.controllers;

import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.UserBasicModel;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.exception.BadRequestException;
import com.hspan.hspan.exception.NotFoundException;
import com.hspan.hspan.exception.UnauthorizedException;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;

@Transactional
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    Auth auth;

    @Autowired
    Snowflake snowflake;

    @Autowired
    UserRepository userRepository;

    @PostMapping("in")
    @ResponseBody
    public QResponse login(@RequestBody UserBasicModel info, HttpSession session) {
        var user = userRepository.findByUsername(info.username);
        if(user == null) {
            throw new NotFoundException("User Not Found");
        }
        if (!user.checkPassword(info.password)) {
            throw new BadRequestException("Wrong Password!");
        }

        session.setAttribute("userID", user.getId());
        session.setAttribute("username", user.getUsername());
        return new QResponse("登录成功！", user.getId(), "200", true);
    }

    @GetMapping("out")
    @ResponseBody
    public QResponse logout(@RequestBody UserBasicModel info, HttpSession session) {
        if (session.getAttribute("userID") == null || session.getAttribute("username") == null) {
            throw new UnauthorizedException();
        }
        Long id = (Long)session.getAttribute("userID");

        session.removeAttribute("userID");
        session.removeAttribute("username");
        return new QResponse("注销成功！", id, "200", true);
    }
}
