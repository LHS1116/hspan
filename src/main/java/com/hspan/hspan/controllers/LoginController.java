package com.hspan.hspan.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspan.hspan.HsConfig;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.UserBasicModel;
import com.hspan.hspan.dto.out.MResponse;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.CasServer;
import com.hspan.hspan.services.HttpUtil;
import com.hspan.hspan.services.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;


@Transactional
@RestController
@RequestMapping("/api/login/")
public class LoginController {
    @Autowired
    Auth auth;

    @Autowired
    HsConfig hsConfig;

    @Autowired
    Snowflake snowflake;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpUtil httpUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CasServer casServer;


    @GetMapping("test")
    @ResponseBody
    public String test() {
        System.out.println("test");
        return "Login Test!";
    }

//    @PostMapping("in")
//    @ResponseBody
//    public QResponse login(@RequestBody UserBasicModel info, HttpSession session) {
//        var user = userRepository.findByUsername(info.username);
//        System.out.println(info.password + info.username);
//        if(user == null) {
//            return QResponse.notFoundResponse("username");
//
//        }
//        if (!user.checkPassword(info.password)) {
//            return new QResponse("密码错误", -1L, "200", false);
//
//        }
//        session.setAttribute("userID", user.getId());
//        session.setAttribute("username", user.getUsername());
//        return new QResponse("登录成功！", user.getId(), "200", true);
//    }

    @GetMapping("cas/in")
    public QResponse casIn(HttpServletResponse response, HttpSession session) throws IOException, URISyntaxException {
        var token = session.getAttribute("casToken");
        if (token == null) {
            response.sendRedirect("localhost:8087/");
        }
        var resp = httpUtil.doGet(hsConfig.getCasServer() + "/api/sso/verify", new HashMap<>() {{
            put("casToken", token);
        }});
        if (resp != null) {
            MResponse mr = objectMapper.readValue(resp, MResponse.class);
            session.setAttribute("isLogin", true);
            session.setAttribute("userName", mr.getData());
        }
        return QResponse.unauthorizedResponse();
    }


    @PostMapping("in")
    @ResponseBody
    public QResponse login(@RequestBody UserBasicModel model,HttpServletResponse response, HttpSession session) throws IOException, URISyntaxException {
        var token = session.getAttribute("casToken");
        if (token == null) {
            var params = new HashMap<String ,String>() {{
                put("username", model.username);
                put("password", model.password);
            }};
            var resp = casServer.casLogin(params);
            if (resp != null) {
//                System.out.println(resp);
//                MResponse mr = objectMapper.readValue(resp, MResponse.class);
                if (resp.getCode() == 0) {
                    response.setHeader("Access-Control-Allow-Credentials", "true");
                    response.addCookie(new Cookie("casToken", (String)resp.getData()));
                    session.setAttribute("isLogin", true);
                    session.setAttribute("casToken", resp.getData());
//                    session.setAttribute("username", mr.getData());
                    return new QResponse(model.username, 200L, "login success", true);
                }
            }
        } else {
            var resp = casServer.casTokenVerify((String) token);
            System.out.println(resp);
            if (resp != null) {
//                MResponse mr = objectMapper.readValue(resp, MResponse.class);
                Cookie cookie = new Cookie("casToken", (String) token);
                cookie.setDomain("lhs.com");
                cookie.setPath("/");
                session.setAttribute("isLogin", true);
                session.setAttribute("username", resp.getData());
                response.addCookie(cookie);
                return new QResponse(resp.getData(), -1L, "Success", true);
            }
        }
        return QResponse.unauthorizedResponse();

//        if (!user.checkPassword(info.password)) {
//            return new QResponse("密码错误", -1L, "200", false);
//
//        }
//        session.setAttribute("userID", user.getId());
//        session.setAttribute("username", user.getUsername());
//        return new QResponse("登录成功！", user.getId(), "200", true);
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
