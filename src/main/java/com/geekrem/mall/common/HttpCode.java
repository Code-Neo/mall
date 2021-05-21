package com.geekrem.mall.common;

import lombok.Data;

/**
 * 后台Http相应代码枚举类
 */

public enum HttpCode {
    CODE_SUCCESS(200),CODE_FAIL(500),CODE_ERROR(500),CODE_NO_LOGIN(300);

    private int code;

    private HttpCode(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
