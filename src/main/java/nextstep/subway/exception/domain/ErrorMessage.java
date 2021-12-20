package nextstep.subway.exception.domain;

public class ErrorMessage {

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public static ErrorMessage of(Exception e) {
        return new ErrorMessage(e.getMessage());
    }

    public static ErrorMessage of(String message) {
        return new ErrorMessage(message);
    }

    public String getMessage() {
        return message;
    }
}
