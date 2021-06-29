package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum CustomExceptionMessage {
    NONE(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    NOT_FOUND_LINE(HttpStatus.BAD_REQUEST, "해당 라인을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "해당 멤버를 찾을 수 없습니다."),
    NOT_FOUND_STATION(HttpStatus.BAD_REQUEST, "해당 지하철 역을 찾을 수 없습니다."),
    OVER_DISTANCE(HttpStatus.BAD_REQUEST, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    IMPOSSIBLE_MIN_SECTION_SIZE(HttpStatus.BAD_REQUEST, "구간이 한개 일때는 삭제할 수 없습니다."),
    EXIST_ALL_STATION_IN_SECTIONS(HttpStatus.BAD_REQUEST, "이미 등록된 구간 입니다."),
    NOT_EXIST_ALL_STATION_IN_SECTIONS(HttpStatus.BAD_REQUEST, "등록할 수 없는 구간 입니다."),
    SAME_SOURCE_AND_TARGET_STATION(HttpStatus.BAD_REQUEST, "경로 조회의 출발 역과 도착 역이 똑같습니다."),
    NOT_FOUND_PATHS(HttpStatus.INTERNAL_SERVER_ERROR, "경로 조회를 할 수 없습니다."),
    NOT_CONNECTED_SOURCE_AND_TARGET_STATION(HttpStatus.BAD_REQUEST, "출발 역과 도착 역이 연결되어 있지 않습니다.");

    private final HttpStatus status;
    private final String message;

    CustomExceptionMessage(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
