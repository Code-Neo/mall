package com.geekrem.mall.service.impl;

import com.geekrem.mall.bean.UmsAdmin;
import com.geekrem.mall.bean.UmsAdminExample;
import com.geekrem.mall.bean.UmsPermission;
import com.geekrem.mall.common.JwtTokenUtil;
import com.geekrem.mall.dao.UmsAdminRoleRelationDao;
import com.geekrem.mall.mbg.mapper.UmsAdminMapper;
import com.geekrem.mall.service.UmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * UmsAdminService实现类
 * @author geekrem
 * @version 1.0
 * @date 2021/5/21 19:27
 */
@Service
@Slf4j
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UmsAdminMapper adminMapper;


    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    /**
     * 根据用户名获取后台管理员
     *
     * @param username
     */
    @Override
    public UmsAdmin getAdminByUsername (String username) {
        UmsAdminExample adminExample = new UmsAdminExample ();
        adminExample.createCriteria ().andUsernameEqualTo (username);
        List<UmsAdmin> adminList = adminMapper.selectByExample (adminExample);
        if (adminList != null && adminList.size() > 0){
            return adminList.get (0);
        }
        return null;
    }

    /**
     * 注册功能
     * @param umsAdminParam
     */
    @Override
    public UmsAdmin register (UmsAdmin umsAdminParam) {
        UmsAdmin newAdmin = new UmsAdmin ();
        BeanUtils.copyProperties (umsAdminParam,newAdmin);
        newAdmin.setCreateTime (new Date ());
        newAdmin.setStatus (1);
        // 查询是否存在相同的用户名或用户
        UmsAdminExample adminExample = new UmsAdminExample ();
        adminExample.createCriteria ().andUsernameEqualTo (newAdmin.getUsername ());
        List<UmsAdmin> umsAdmins = adminMapper.selectByExample (adminExample);
        if (umsAdmins != null && umsAdmins.size () > 0) {
            // 存在重复的用户名
            return null;
        }
        // 将密码进行加密
        String encodePassword = passwordEncoder.encode (newAdmin.getPassword ());
        newAdmin.setPassword (encodePassword);
        adminMapper.insert (newAdmin);
        return newAdmin;
    }

    /**
     * 登录功能 @param username 用户名 @param password 密码 @return 生成的JWT的token
     *
     * @param username
     * @param password
     */
    @Override
    public String login (String username, String password) {
        log.debug ("username={},password={}",username,password);
        String token =null;
        // 根据用户名查询用户
        try{
            UserDetails userDetails = userDetailsService.loadUserByUsername (username);
            if (!passwordEncoder.matches (password,userDetails.getPassword ())) {
                throw new BadCredentialsException ("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken (userDetails, null, userDetails.getAuthorities ());
            SecurityContextHolder.getContext ().setAuthentication (authenticationToken);
            token = jwtTokenUtil.generateToken (userDetails);
        }catch (AuthenticationException e){
            log.debug ("登录异常：{}",e.getMessage ());
        }
        return token;
    }

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     *
     * @param adminId
     */
    @Override
    public List<UmsPermission> getPermissionList (Long adminId) {
        return adminRoleRelationDao.getPermissionList (adminId);
    }
}
