package nextstep.subway.line.domain;

public enum LineExceptionType {

    CANNOT_EMPTY_LINE_NAME("노선 명은 null 혹은 빈문자열 일 수 없습니다."),
    CANNOT_EMPTY_LINE_COLOR("노선 색상은 null 혹은 빈문자열 일 수 없습니다."),
    DISTANCE_IS_MUST_BE_GREATER_THAN_1("구간의 거리는 1보다 큰 정수이어야 합니다."),

    NOT_FOUND_UP_STATION_BY_SECTION("상행역이 존재하지 않습니다."),
    NOT_FOUND_SECTION("해당되는 구간을 찾지 못하였습니다"),
    ALREADY_ADDED_SECTION("이미 등록된 구간 입니다."),
    CANNOT_ADDED_SECTION("등록할 수 없는 구간 입니다."),
    NOT_EXIST_FIRST_SECTION("첫 번째 구간이 없습니다."),

    CANNOT_REMOVE_STATION_WHEN_ONLY_ONE_SECTIONS("노선의 구간이 1개만 존재할 경우, 지하철 역을 삭제할 수 없습니다."),
    CANNOT_REMOVE_STATION_IS_NOT_EXIST("노선에 존재하지 않은 지하철 역은 삭제할 수 없습니다."),
    NEED_NARROW_DISTANCE_THAN_SECTION("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")
    ;

    private String message;

    LineExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
