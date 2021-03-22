package com.hspan.hspan.services;


import com.hspan.hspan.data.repos.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 读取cookie来判断用户是否登录、并提供用户的id
 */
public class Auth {
    /**
     * -1 则代表未登录
     */
    private long userID = -1;

    /**
     * 是否加载过
     * Auth类采用懒加载，构造对象时不会去查询数据库去验证用户的密码
     */
    private boolean loaded = false;

    private HttpServletRequest request;
    private UserRepository userRepository;
    private HttpServletResponse response;

    public Auth(HttpServletRequest request, UserRepository userRepository) {
        this.request = request;
        this.userRepository = userRepository;
    }

    private boolean innerLoad() {
        if (loaded) {
            throw new IllegalStateException();
        }
//        var cookies = request.getCookies();
        var session = request.getSession();
        if (session == null) {
            return false;
        }

        var username = session.getAttribute("username");
        var userID = session.getAttribute("userID");

        if (username == null || userID == null) {
            return false;
        }

        var user = userRepository.findByUsername((String)username);

        if (user == null) {
            return false;
        }

        if (!user.getId().equals(userID)) {
            return false;
        }

        // username and password matches
        this.userID = user.getId();

        return true;
    }

    private void load() {
        var success = innerLoad();
        loaded = true;
        // if not success, clear some cookie
//        if (!success) {
//            var cookie1 = new Cookie("username", null);
//            cookie1.setPath("/");
//
//            var cookie2 = new Cookie("password", null);
//            cookie2.setPath("/");
//
//            response.addCookie(cookie1);
//            response.addCookie(cookie2);
//        }
    }

    public boolean isLoggedIn() {
        if (!loaded) {
            load();
        }
        return userID != -1;
    }

    public long userId() {
        if (!loaded) {
            load();
        }
        if (!isLoggedIn()) {
            throw new IllegalStateException();
        }

        return userID;
    }
}
