package com.hspan.hspan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspan.hspan.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        //这里实际是从redis中获取到session信息
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("/login/test");
//            response.sendError(401, new UnauthorizedException().toString());
//            throw new UnauthorizedException();
            return false;
        }
        return true;
    }

    /**
     * json输出
     *
     * @param response
     * @throws IOException
     */
//    private void authFailOutput(HttpServletResponse response, String msg) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        out.write(objectMapper.writeValueAsString(CommonResVo.fail(400, msg)));
//        out.flush();
//    }
}
