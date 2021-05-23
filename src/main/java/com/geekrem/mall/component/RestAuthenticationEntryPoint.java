package com.geekrem.mall.component;

import cn.hutool.json.JSONUtil;
import com.geekrem.mall.common.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * @author geekrem
 * @version 1.0
 * @date 2021/5/21 19:19
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding ("UTF-8");
        response.setContentType ("application/json");
        response.getWriter ().println (JSONUtil.parse (CommonResult.unauthorized (e.getMessage (),null)));
        response.getWriter ().flush ();
    }
}
