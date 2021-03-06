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
        http.csrf ()                    // ??????????????????JWT????????????????????????csrf
                .disable ()
                .sessionManagement ()   // ??????token??????????????????session
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
                .permitAll ()           // ????????????????????????????????????????????????
                .antMatchers ("/admin/login","/admin/register")
                .permitAll ()           // ????????????????????????????????????
                .antMatchers (HttpMethod.OPTIONS)   //??????????????????????????????options??????
                .permitAll ()
                .anyRequest ()
                .authenticated ();      // ???????????????????????????????????????????????????
        //      .antMatchers("/**")//???????????????????????????
        //      .permitAll()

        // ????????????
        http.headers ().cacheControl ();

        // ??????JWT filter
        http.addFilterBefore (jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //????????????????????????????????????????????????
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
        //????????????????????????
        return username ->{
            UmsAdmin admin = umsAdminService.getAdminByUsername (username);
            if (admin != null) {
                List<UmsPermission> permissionList = umsAdminService.getPermissionList (admin.getId ());
                return new AdminUserDetails (admin,permissionList);
            }
            throw  new UsernameNotFoundException ("????????????????????????");
        };
    }
}

