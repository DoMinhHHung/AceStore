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