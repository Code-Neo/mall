package com.geekrem.mall.controller;

import com.geekrem.mall.bean.UmsAdmin;
import com.geekrem.mall.bean.UmsPermission;
import com.geekrem.mall.common.CommonResult;
import com.geekrem.mall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 后台用户管理
 * @author geekrem
 * @version 1.0
 * @date 2021/5/22 17:16
 */
@Api(tags = "后台用户管理")
@RestController
@Slf4j
@RequestMapping("/admin")
public class UmsAdminController {
    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation ("用户注册")
    @PostMapping("/register")
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdmin newAdmin,
                                           BindResult result){
        UmsAdmin register = adminService.register (newAdmin);
        if (register != null) {
            CommonResult.success ("注册成功",register);
        }
        return CommonResult.faile ("注册失败",null);
    }

    @ApiOperation ("用户登录(登录以后返回token)")
    @PostMapping("/login")
    public CommonResult login(@RequestBody UmsAdmin loginUser ){
        String token = adminService.login (loginUser.getUsername (), loginUser.getPassword ());
        if (token == null) {
            return CommonResult.faile ("用户名或密码错误",null);
        }
        HashMap<String, String> tokenMap = new HashMap<> ();
        tokenMap.put ("token",token);
        tokenMap.put ("tokenHead",tokenHead);
        return CommonResult.success ("登录成功",tokenMap);
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @GetMapping("/permission/{adminId}")
    public CommonResult getAllPermission(@PathVariable("adminId") Long adminId){
        List<UmsPermission> permissionList = adminService.getPermissionList (adminId);
        return CommonResult.success (adminId+" 的权限",permissionList);
    }
}
