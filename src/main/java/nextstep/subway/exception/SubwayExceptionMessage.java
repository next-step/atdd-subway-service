package nextstep.subway.exception;

public enum SubwayExceptionMessage {
    SAME_SOURCE_TARGET("출발역과 도착역이 동일합니다."),
    EMPTY_SHORTEST_PATH("최단거리가 존재하지 않습니다."),
    EMPTY_SOURCE("출발역이 존재하지 않습니다."),
    EMPTY_TARGET("도착역이 존재하지 않습니다."),
    TOO_BIG_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    DUPLICATE_SECTION("이미 등록된 구간 입니다."),
    INVALID_SECTION("등록할 수 없는 구간 입니다."),
    CANNOT_DELETE_LAST_SECTION("마지막 구간은 삭제할 수 없습니다."),
    EMPTY_NEXT_SECTION("다음 구간이 존재하지 않습니다."),
    EMPTY_PREVIOUS_SECTION("이전 구간이 존재하지 않습니다."),
    INVALID_TOKEN("토큰 인증에 실패하였습니다.");

    private final String message;

    SubwayExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
