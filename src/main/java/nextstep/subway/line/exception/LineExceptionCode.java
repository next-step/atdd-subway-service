package nextstep.subway.line.exception;

public enum LineExceptionCode {
    REQUIRED_NAME("The line name is a required field."),
    REQUIRED_COLOR("The line color is a required field."),
    NOT_FOUND_BY_ID("the line not found by id."),
    DO_NOT_ALLOW_NEGATIVE_NUMBER_FARE("The fare does not allow negative number.");

    private String title = "[ERROR] ";
    private String message;

    LineExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return title + message;
    }
}
