package com.geekrem.mall.common;

import lombok.Data;

/**
 * @param <T>
 * 后台默认响应类
 */

@Data
public class CommonResult<T> {

    private int code;

    private String msg;

    private T data;


    public static  <T> CommonResult<T>  success(String msg,T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(HttpCode.CODE_SUCCESS.getCode());
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static  <T> CommonResult<T>  faile(String msg,T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(HttpCode.CODE_FAIL.getCode());
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static  <T> CommonResult<T>  error(String msg,T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(HttpCode.CODE_ERROR.getCode());
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static  <T> CommonResult<T>  noLogin(String msg,T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(HttpCode.CODE_NO_LOGIN.getCode());
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

}
