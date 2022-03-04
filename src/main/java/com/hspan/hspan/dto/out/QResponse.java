package com.hspan.hspan.dto.out;

public class QResponse {

    private Object data;
    private Long id;
    private String status;
    private boolean success;
    private int code = 200;

    public QResponse(Object data, Long id, String status, boolean success) {
        this.data = data;
        this.id = id;
        this.status = status;
        this.success = success;
    }

    public QResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public static QResponse unauthorizedResponse() {
        return new QResponse(null, -1L, "请先登录！", false).setCode(403);
    }

    public static QResponse notFoundResponse() {
        return new QResponse(null, -1L, "Not Found!", false);
    }
    public static QResponse notFoundResponse(String msg) {
        return new QResponse(null, -1L, msg + " Not Found!", false);
    }

    public static QResponse redirectResponse(String url) {
        return new QResponse(url, -1L, "redirect", false);
    }
}
