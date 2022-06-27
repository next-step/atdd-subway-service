package nextstep.subway.station.exception;

public enum StationExceptionType {
    NOT_FOUND("해당 역이 존재하지 않습니다.");

    private final String message;

    StationExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
