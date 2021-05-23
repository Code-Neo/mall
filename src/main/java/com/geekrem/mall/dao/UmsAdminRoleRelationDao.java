package com.geekrem.mall.dao;

import com.geekrem.mall.bean.UmsPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户与角色管理自定义Dao
 * @author geekrem
 * @version 1.0
 * @date 2021/5/22 15:54
 */

public interface UmsAdminRoleRelationDao {

    /**
     * 获取用户所有权限(包括+-权限)
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
}
