package nextstep.subway.fare.exception;

public enum AgeFareExceptionCode {
    NONE_EXISTS_AGE("해당하는 나이가 없습니다");

    private final String message;

    AgeFareExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
