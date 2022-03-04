package com.hspan.hspan.services;


import com.hspan.hspan.data.repos.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * 读取session来判断用户是否登录、并提供用户的id
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
//        var userID = session.getAttribute("userID");
        System.out.println("Auth username: " + username);
        if (username == null) {

            return false;
        }

        var user = userRepository.findByUsername((String)username);

        if (user == null) {
            return false;
        }

//        if (!user.getId().equals(userID)) {
//            return false;
//        }

        // username and password matches
        this.userID = user.getId();

        return true;
    }

    private void load() {
        var success = innerLoad();
        loaded = true;
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
    public String formatDate(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
    }
}
