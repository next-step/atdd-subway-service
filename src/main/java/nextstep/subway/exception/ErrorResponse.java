package nextstep.subway.exception;

public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse() {

    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
