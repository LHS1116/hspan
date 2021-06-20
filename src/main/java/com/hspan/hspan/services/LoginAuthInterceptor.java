package com.hspan.hspan.services;

import com.hspan.hspan.dto.out.QResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

//@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    Auth auth;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (!auth.isLoggedIn()) {
            authFailOutput(response);
            return false;
        }
        return true;
    }

    private void authFailOutput(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.write(QResponse.unauthorizedResponse().toString());
        out.flush();
    }
}
