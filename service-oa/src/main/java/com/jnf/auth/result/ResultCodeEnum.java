package com.jnf.auth.result;

import lombok.Getter;

/**
 * @author jnfstart
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    SERVICE_ERROR(2012,"服务异常"),
    LOGIN_ERROR(208,"认证失败"),

    //LOGIN_AUTH(208,"未登录"),
    //PERMISSION(209,"没有权限")
    ;


    private Integer code ;

    private String message ;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
