package com.geekrem.mall.service.impl;

import com.geekrem.mall.common.CommonResult;
import com.geekrem.mall.service.RedisService;
import com.geekrem.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author geekrem
 * @version 1.0
 * @date 2021/5/20 19:02
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private RedisService redisService;

    @Value ("${redis.key.prefix.authCode}")
    private String  REDIS_KEY_PREFIX_AUTH_CODE;

    @Value ("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode (String phoneNum) {
        // 获取验证码
        StringBuilder sb = new StringBuilder ();
        Random random = new Random ();
        for (int i = 0; i < 5; i++) {
            sb.append (random.nextInt (10));
        }

        // 把验证码放到Redis中，并设置过去时间为120秒
        redisService.set (REDIS_KEY_PREFIX_AUTH_CODE+phoneNum,sb.toString ());
        redisService.expire (REDIS_KEY_PREFIX_AUTH_CODE+phoneNum,AUTH_CODE_EXPIRE_SECONDS);

        return CommonResult.success ("验证码获取成功",sb.toString ());
    }

    /**
     * 验证验证码是否正确
     * @param phoneNum
     * @param authCode
     * @return
     */
    @Override
    public CommonResult verifyAuthCode (String phoneNum, String authCode) {
        String authCodeForRedis = redisService.get (REDIS_KEY_PREFIX_AUTH_CODE + phoneNum);
        if (authCodeForRedis == null) {
            return CommonResult.error ("验证码已过期，请重新获取！",null);
        }else if(authCode.equals (authCodeForRedis)){
            return  CommonResult.success ("验证通过",authCodeForRedis);
        }else{
            return CommonResult.faile ("验证码错误，请重新输入",null);
        }
    }


}
