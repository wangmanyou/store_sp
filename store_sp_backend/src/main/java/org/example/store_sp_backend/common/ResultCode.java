package org.example.store_sp_backend.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录失效"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "数据不存在"),
    CONFLICT(409, "数据冲突"),
    ERROR(500, "系统异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
