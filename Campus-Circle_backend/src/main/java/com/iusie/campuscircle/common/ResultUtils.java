package com.iusie.campuscircle.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok");
    }

    /**
     * 失败
     *
     * @param stateCode
     * @return
     */
    public static BaseResponse error(StateCode stateCode) {
        return new BaseResponse<>(stateCode);
    }
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse(code,null,message,description);
    }
    public static BaseResponse error(StateCode stateCode,String message,String description){
        return new BaseResponse(stateCode.getCode(),null,message,description);
    }
    public static BaseResponse error(StateCode stateCode,String description){
        return new BaseResponse(stateCode.getCode(),null,stateCode.getMessage(),description);
    }
}
