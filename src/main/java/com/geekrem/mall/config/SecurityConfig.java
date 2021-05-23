package com.geekrem.mall.config;

import com.geekrem.mall.bean.UmsAdmin;
import com.geekrem.mall.bean.UmsPermission;
import com.geekrem.mall.component.MyJwtAuthenticationTokenFilter;
import com.geekrem.mall.component.RestAuthenticationEntryPoint;
import com.geekrem.mall.component.RestfulAccessDeniedHandler;
import com.geekrem.mall.dto.AdminUserDetails;
import com.geekrem.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @author geekrem
 * @version 1.0
 * @date 2021/5/21 18:43
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Lazy
    @Autowired
    private UmsAdminService umsAdminService;

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http.csrf ()                    // 由于使用的是JWT，我们这里不需要csrf
                .disable ()
                .sessionManagement ()   // 基于token，所以不需要session
                .sessionCreationPolicy (SessionCreationPolicy.STATELESS)
                .and ()
                .authorizeRequests ()
                .antMatchers (HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "swagger-ui.html",
                        "/v2/**")
                .permitAll ()           // 允许对于网站静态资源的无授权访问
                .antMatchers ("/admin/login","/admin/register")
                .permitAll ()           // 对登录注册要允许匿名访问
                .antMatchers (HttpMethod.OPTIONS)   //跨域请求会先进行一次options请求
                .permitAll ()
                .anyRequest ()
                .authenticated ();      // 除上面外的所有请求全部需要鉴权认证
        //      .antMatchers("/**")//测试时全部运行访问
        //      .permitAll()

        // 禁用缓存
        http.headers ().cacheControl ();

        // 添加JWT filter
        http.addFilterBefore (jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling ()
                .accessDeniedHandler (restfulAccessDeniedHandler)
                .authenticationEntryPoint (restAuthenticationEntryPoint);
    }

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService (userDetailsService ())
                .passwordEncoder (passwordEncoder ());
    }


    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder ();
    }

    @Bean
    public MyJwtAuthenticationTokenFilter jwtAuthenticationTokenFilter () {
        return new MyJwtAuthenticationTokenFilter ();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean () throws Exception {
        return super.authenticationManagerBean ();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        //获取登录用户信息
        return username ->{
            UmsAdmin admin = umsAdminService.getAdminByUsername (username);
            if (admin != null) {
                List<UmsPermission> permissionList = umsAdminService.getPermissionList (admin.getId ());
                return new AdminUserDetails (admin,permissionList);
            }
            throw  new UsernameNotFoundException ("用户名或密码错误");
        };
    }
}

