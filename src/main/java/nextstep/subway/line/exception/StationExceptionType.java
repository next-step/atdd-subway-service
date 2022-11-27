package nextstep.subway.line.exception;

public enum StationExceptionType {
    STATION_NOT_FOUND("해당 지하철 역이 존재하지 않습니다.");

    private final String message;

    StationExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
