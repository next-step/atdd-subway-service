package nextstep.subway.fare.exception;

public enum DistanceFareExceptionCode {
    NONE_EXISTS_DISTANCE("해당하는 거리가 없습니다");

    private final String message;

    DistanceFareExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
