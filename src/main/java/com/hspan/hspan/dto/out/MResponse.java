package com.hspan.hspan.dto.out;

public class MResponse {
    private int code;
    private String message;
    private Object data;

    public MResponse() {}
    public MResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public static MResponse successResponse() {
        return new MResponse(0, "success", null);
    }
    public static MResponse notFoundResponse(String entityType) {
        return new MResponse(1000, entityType + "not found!", null);
    }

    public static MResponse unauthorizedResponse() {
        return new MResponse(1001, "请先登录！", null);
    }

    public static MResponse authorizeFailedResponse() {
        return new MResponse(1002, "认证失败", null);
    }

    public static MResponse duplicateEntityResponse(String entityType) {
        return new MResponse(1003, entityType + " exists!", null);

    }
}
