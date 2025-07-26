package com.sky.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
@Component
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;

    public JwtTokenAdminInterceptor(JwtProperties jwtProperties) {
        // 将IOC容器中的jwtProperties对象赋值给本类的jwtProperties属性
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入管理员拦截器");
        // 1.1 获取请求路径
        String uri = request.getRequestURI();
        // 1.2 判断请求路径是否包含"/login"
        if (uri.contains("/login")) {
            return true;
        }
        // 1.4 如果不包含，判断请求头中的token是否可用
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        // 1.5 判断token是否存在。如果不存在则返回错误响应
        if (!StringUtils.hasText(token)) {
            Result<String> result = Result.error(MessageConstant.USER_NOT_LOGIN);
            // 将实例转化为json格式
            String notLogin = JSONObject.toJSONString(result);
            // 将json数据放入响应体中
            response.getWriter().write(notLogin);
            return false;
        }
        // 1.6 若token存在，则对其进行解析操作
        try {
            Map<String, Object> claims = JwtUtil.parseJWT(jwtProperties.getAdminkey(), token);
            // 将JWT中的id解析出来，存入ThreadLocal（当前线程的局部变量），方便后续使用
            Integer empID = (Integer) claims.get(JwtClaimsConstant.EMP_ID);
            BaseContext.setCurrentId(empID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 不通过，设置相应状态码:表示当前token为错误或过期令牌
            response.setStatus(401);
            return false;
        }
    }
}
