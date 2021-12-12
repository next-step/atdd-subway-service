package nextstep.subway.common.exception;

public enum SubwayErrorCode {
    SAME_SOURCE_AND_TARGET("출발역과 도착역이 같습니다."),
    ALREADY_REGISTERED_SECTION("이미 등록된 구간 입니다."),
    INVALID_LINE_SECTION("등록할 수 없는 구간 입니다."),
    CANNOT_DELETE_LAST_LINE("구간이 하나인 노선에서 마지막 구간을 제거할 수 없습니다."),
    NOT_LOWER_THAN_ORIGINAL_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    INVALID_DISTANCE("1 이상의 길이만 입력 가능합니다.");

    private final String message;

    SubwayErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

