package com.example.demo.controller;

import com.example.demo.VO.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    /**
     * 设置存储的 SESSION 名称
     */
    public static final String USER_SESSION_NAME = "user";
    /**
     * 设置模拟用户信息
     */
    private static final Map<String, User> USER_MAP = new HashMap<>();

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        User user1 = new User();
        user1.setUsername("andy1");
        user1.setPassword("89230056");
        User user2 = new User();
        user2.setUsername("andy2");
        user2.setPassword("89230056");
        
        User user3 = new User();
        user3.setUsername("andy3");
        user3.setPassword("89230056");        
        USER_MAP.put(user1.getUsername(), user1);
        USER_MAP.put(user2.getUsername(), user2);
        USER_MAP.put(user3.getUsername(), user3);
    }
    
    
    
    @RequestMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 检测是否已经登录
        if (request.getSession().getAttribute(USER_SESSION_NAME) != null) {
            return "index";
        }
        // 验证用户名密码，如果验证失败则返回错误信息
        if (validate(username, password)) {
            // 验证成功则将登录用户信息存储
            request.getSession().setAttribute(USER_SESSION_NAME, USER_MAP.get(username));
            request.getSession().setAttribute("userName", username);
            // 跳转到首页
            return "index";
//            response.sendRedirect("index");
        }
        return "login";
    }

    /**
     * 验证用户名、密码是否正确
     *
     * @param username 用户名
     * @param password 密码
     */
    private boolean validate(String username, String password) {
        // 验证用户名密码，如果验证失败则返回错误信息
        for (User user : USER_MAP.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}