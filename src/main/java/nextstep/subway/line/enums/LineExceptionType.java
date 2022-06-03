package nextstep.subway.line.enums;

public enum LineExceptionType {

    CANNOT_EMPTY_LINE_NAME("노선 명은 null 혹은 빈문자열 일 수 없습니다."),
    CANNOT_EMPTY_LINE_COLOR("노선 색상은 null 혹은 빈문자열 일 수 없습니다."),
    DISTANCE_IS_MUST_BE_GREATER_THAN_1("구간의 거리는 1보다 큰 정수이어야 합니다.");

    private String message;

    LineExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
