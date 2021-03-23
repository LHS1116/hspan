package com.hspan.hspan.dto.out;

public class QResponse {

    private Object data;
    private Long id;
    private String status;
    private boolean success;

    public QResponse(Object data, Long id, String status, boolean success) {
        this.data = data;
        this.id = id;
        this.status = status;
        this.success = success;
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
}
