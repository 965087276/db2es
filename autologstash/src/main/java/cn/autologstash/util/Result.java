package cn.autologstash.util;

public class Result {
    public static int SUCCESS_CODE = 0;
    public static int FAIL_CODE = 1;

    int code;
    String message;
    Object data;

    public Result() {}
    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result Success() {
        return new Result(SUCCESS_CODE,null,null);
    }
    public static Result Success(Object data) {
        return new Result(SUCCESS_CODE,"",data);
    }
    public static Result Fail(String message) {
        return new Result(FAIL_CODE,message,null);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}