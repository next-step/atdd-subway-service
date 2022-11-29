package nextstep.subway.line.dto;

public class ErrorResponse {


    private final String status;
    private final int code;
    private final String message;

    private ErrorResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    public static ErrorResponse of(String status, int code, String message){
        return new ErrorResponse(status, code, message);
    }

    public String getMessage() {
        return message;
    }
}
