package nextstep.subway.exception.dto;

public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(final String error, final String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
