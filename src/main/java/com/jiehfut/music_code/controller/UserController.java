package com.jiehfut.music_code.controller;


import com.jiehfut.music_code.mapper.UserMapper;
import com.jiehfut.music_code.model.User;
import com.jiehfut.music_code.utils.Constant;
import com.jiehfut.music_code.utils.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/login-test")
    public ResponseBodyMessage<User> loginTest(@RequestParam String username, @RequestParam String password,
                                            HttpServletRequest request) {
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        // 从数据库中根据用户信息进行查询
        User user = userMapper.login(loginUser);
        if (user != null) {
            System.out.println("success");
            HttpSession session = request.getSession();
            session.setAttribute(Constant.USERINFO_SESSION_KEY, user);

            return new ResponseBodyMessage<>(1, "登陆成功！", loginUser);
        } else {
            System.out.println("fail");
            return new ResponseBodyMessage<>(0, "登陆失败！", loginUser);
        }
    }


    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password,
                                           HttpServletRequest request) {

        // 从数据库中根据用户信息进行查询
        User user = userMapper.findUserByUsername(username);
        if (null == user) {
            // 数据库中没有该用户
            return new ResponseBodyMessage<>(-1, "无此用户", user);
        } else {
            // 检验密码是否正确
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                // 密码正确
                HttpSession session = request.getSession();
                session.setAttribute(Constant.USERINFO_SESSION_KEY, user);
                return new ResponseBodyMessage<>(0, "登陆成功", user);
            } else {
                // 密码错误
                return new ResponseBodyMessage<>(-1, "密码错误", user);
            }
        }
    }







}
