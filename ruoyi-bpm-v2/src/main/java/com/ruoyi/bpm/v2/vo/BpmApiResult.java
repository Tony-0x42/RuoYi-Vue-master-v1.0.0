package com.ruoyi.bpm.v2.vo;

import java.util.UUID;

/**
 * 流程中台统一 API 响应结构
 */
public class BpmApiResult<T> {

    /** 业务状态码，0 表示成功 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 业务数据 */
    private T data;

    /** 请求追踪 ID */
    private String traceId;

    public BpmApiResult() {
        this.traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    public static <T> BpmApiResult<T> ok(T data) {
        BpmApiResult<T> result = new BpmApiResult<>();
        result.setCode(0);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> BpmApiResult<T> error(int code, String message) {
        BpmApiResult<T> result = new BpmApiResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
