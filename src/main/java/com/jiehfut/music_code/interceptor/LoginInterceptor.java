package com.jiehfut.music_code.interceptor;


import com.jiehfut.music_code.utils.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if(session != null &&
                session.getAttribute(Constant.USERINFO_SESSION_KEY)!=null) {
            //此时是登录状态，放行
            return true;
        }
        // 拦截，跳转到登陆页面
        response.sendRedirect("/login.html");
        return false;
    }


}

