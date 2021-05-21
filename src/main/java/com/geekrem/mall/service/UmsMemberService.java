package com.geekrem.mall.service;

import com.geekrem.mall.common.CommonResult;

/**
 * @author geekrem
 * @version 1.0
 * @date 2021/5/20 19:00
 * 电话验证码管理服务
 */
public interface UmsMemberService {
    /**
     * 获取电话验证码
     * @param phoneNum
     * @return
     */
    CommonResult generateAuthCode (String phoneNum);

    /**
     * 验证验证码是否正确
     * @param phoneNum
     * @param authCode
     * @return
     */
    CommonResult verifyAuthCode (String phoneNum, String authCode);
}
