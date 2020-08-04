package com.salhe.summer.antibigdata.dto;

import lombok.Data;

@Data
public class ApiResult {

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int BAD_REQUEST = 400;
    public static final int PERMISSION_DENIED = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_SERVER_ERROR = 500;

    private int code;
    private String msg;
    private Object data;

    public ApiResult data(Object data) {
        this.data = data;
        return this;
    }

    public ApiResult msg(String msg) {
        this.msg = msg;
        return this;
    }

    private ApiResult(int code) {
        this.code = code;
    }

    public static ApiResult success() {
        return new ApiResult(SUCCESS).msg("success");
    }

    public static ApiResult error() {
        return new ApiResult(ERROR).msg("error");
    }

    public static ApiResult permissionDenied() {
        return new ApiResult(PERMISSION_DENIED).msg("请登录或检查您是否为管理员");
    }

    public static ApiResult notFound() {
        return new ApiResult(NOT_FOUND).msg("未找到");
    }

    public static ApiResult methodNotAllowed() {
        return new ApiResult(METHOD_NOT_ALLOWED).msg("当前请求方式不支持");
    }

    public static ApiResult ise() {
        return new ApiResult(INTERNAL_SERVER_ERROR).msg("ISE，服务器瓦特了~");
    }

    public static ApiResult badRequest() {
        return new ApiResult(BAD_REQUEST).msg("请求参数有误");
    }


}
