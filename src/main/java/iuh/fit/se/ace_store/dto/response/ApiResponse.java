package iuh.fit.se.ace_store.dto.response;

public class ApiResponse {

    private boolean success;
    private String errorCode;
    private String message;
    private String action;
    private Object data;

    public ApiResponse(boolean success, String errorCode, String message, String action, Object data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.action = action;
        this.data = data;
    }
    public static ApiResponse success(String message) {
        return new ApiResponse(true, null, message, null, null);
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, null, message, null, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, null, message, null, null);
    }

    public static ApiResponse error(String message, String errorCode) {
        return new ApiResponse(false, errorCode, message, null, null);
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(true, null, "Success", null, data);
    }


    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }
}