package com.youkang.common.core.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.youkang.common.constant.HttpStatus;
import com.youkang.common.utils.StringUtils;
import lombok.Getter;

/**
 * 有康统一响应结果
 * <p>
 * 整合原 AjaxResult 和 R 类的功能， * 继承 HashMap 支持动态扩展字段（如 token）
 * - 泛型支持
 * - 成功/警告/错误状态
 * - 链式调用
 * </p>
 *
 * @param <T> 数据类型
 * @author youkang
 */
@Getter
public class YKResponse<T> extends HashMap<String, Object> implements Serializable {


    /** 状态码 Key */
    public static final String CODE_TAG = "code";

    /** 返回消息 Key */
    public static final String MSG_TAG = "msg";

    /** 数据对象 Key */
    public static final String DATA_TAG = "data";

    /** 返回数据（泛型） */
    private T data;

    // ==================== 构造方法 ====================

    public YKResponse() {
    }

    public YKResponse(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    public YKResponse(int code, String msg, T data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
            this.data = data;
        }
    }

    // ==================== 成功响应 ====================

    /**
     * 返回成功结果
     */
    public static <T> YKResponse<T> ok() {
        return restResult(null, HttpStatus.SUCCESS, "操作成功");
    }

    /**
     * 返回成功结果（带数据）
     */
    public static <T> YKResponse<T> ok(T data) {
        return restResult(data, HttpStatus.SUCCESS, "操作成功");
    }

    /**
     * 返回成功结果（带消息）
     */
    public static <T> YKResponse<T> ok(String msg) {
        return restResult(null, HttpStatus.SUCCESS, msg);
    }

    /**
     * 返回成功结果（带数据和消息）
     */
    public static <T> YKResponse<T> ok(T data, String msg) {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    /**
     * 返回成功结果（兼容 AjaxResult.success 命名）
     */
    public static <T> YKResponse<T> success() {
        return ok();
    }

    /**
     * 返回成功结果（带数据，兼容 AjaxResult.success 命名）
     */
    public static <T> YKResponse<T> success(T data) {
        return ok(data);
    }

    /**
     * 返回成功结果（带消息，兼容 AjaxResult.success 命名）
     */
    public static <T> YKResponse<T> success(String msg) {
        return ok(msg);
    }

    /**
     * 返回成功结果（带数据和消息，兼容 AjaxResult.success 命名）
     */
    public static <T> YKResponse<T> success(String msg, T data) {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    // ==================== 警告响应 ====================

    /**
     * 返回警告结果
     */
    public static <T> YKResponse<T> warn(String msg) {
        return restResult(null, HttpStatus.WARN, msg);
    }

    /**
     * 返回警告结果（带数据）
     */
    public static <T> YKResponse<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }

    // ==================== 错误响应 ====================

    /**
     * 返回错误结果
     */
    public static <T> YKResponse<T> fail() {
        return restResult(null, HttpStatus.ERROR, "操作失败");
    }

    /**
     * 返回错误结果（带消息）
     */
    public static <T> YKResponse<T> fail(String msg) {
        return restResult(null, HttpStatus.ERROR, msg);
    }

    /**
     * 返回错误结果（带数据）
     */
    public static <T> YKResponse<T> fail(T data) {
        return restResult(data, HttpStatus.ERROR, "操作失败");
    }

    /**
     * 返回错误结果（带数据和消息）
     */
    public static <T> YKResponse<T> fail(T data, String msg) {
        return restResult(data, HttpStatus.ERROR, msg);
    }

    /**
     * 返回错误结果（带状态码和消息）
     */
    public static <T> YKResponse<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 返回错误结果（兼容 AjaxResult.error 命名）
     */
    public static <T> YKResponse<T> error() {
        return fail();
    }

    /**
     * 返回错误结果（带消息，兼容 AjaxResult.error 命名）
     */
    public static <T> YKResponse<T> error(String msg) {
        return fail(msg);
    }

    /**
     * 返回错误结果（带状态码和消息，兼容 AjaxResult.error 命名）
     */
    public static <T> YKResponse<T> error(int code, String msg) {
        return fail(code, msg);
    }

    // ==================== 状态判断 ====================

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return HttpStatus.SUCCESS == this.getCode();
    }

    /**
     * 是否警告
     */
    public boolean isWarn() {
        return HttpStatus.WARN == this.getCode();
    }

    /**
     * 是否错误
     */
    public boolean isError() {
        return HttpStatus.ERROR == this.getCode();
    }

    /**
     * 静态方法：判断响应是否成功
     */
    public static <T> boolean isSuccess(YKResponse<T> response) {
        return response != null && response.isSuccess();
    }

    /**
     * 静态方法：判断响应是否错误
     */
    public static <T> boolean isError(YKResponse<T> response) {
        return response == null || response.isError();
    }

    // ==================== 内部方法 ====================

    private static <T> YKResponse<T> restResult(T data, int code, String msg) {
        YKResponse<T> response = new YKResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    // ==================== 链式调用（重写 HashMap.put） ====================

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public YKResponse<T> put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    // ==================== Getter/Setter ====================

    public int getCode() {
        return (int) super.getOrDefault(CODE_TAG, HttpStatus.ERROR);
    }

    public void setCode(int code) {
        super.put(CODE_TAG, code);
    }

    public String getMsg() {
        return (String) super.get(MSG_TAG);
    }

    public void setMsg(String msg) {
        super.put(MSG_TAG, msg);
    }

    @SuppressWarnings("unchecked")
    public void setData(T data) {
        this.data = data;
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }
}
