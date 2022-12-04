package nextstep.subway.common.exception;

public enum ErrorEnum {
    ERROR_MESSAGE_DEFAULT("오류가 발생하였습니다."),
    SOURCE_AND_TARGET_EQUAL_STATION("출발역과 도착역은 같을 수 없습니다."),
    NOT_CONNECTED_STATIONS("출발역과 도착역이 연결되있지 않습니다."),
    VALID_LINE_LENGTH_GREATER_THAN_ZERO("노선의 길이는 0 이상이어야."),
    VALID_GREATER_OR_EQUAL_LENGTH_BETWEEN_STATION("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    EXISTS_STATION("등록하려는 지하철역이 존재합니다."),
    NOT_EXISTS_STATION("지하철역이 존재하지 않습니다."),
    EXISTS_UP_STATION_AND_DOWN_STATION("상행성과 하행선 모두 존재하지 않습니다."),
    LAST_STATION_NOT_DELETE("마지막 구간은 삭제할 수 없습니다."),
    NOT_EXISTS_NOT_DELETE("마지막 구간은 삭제할 수 없습니다."),
    NOT_EXISTS_EMAIL("로그인 실패! 등록되지 않은 이메일입니다."),
    NOT_MATCH_PASSWORD("로그인 실패! 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    NOT_EXISTS_MEMBER("회원 정보를 찾을 수 없습니다."),
    NOT_EXISTS_FAVORITE("즐겨찾기 정보를 찾을 수 없습니다."),
    DISTANCE_GREATER_ZERO("거리는 0 이상이어야 합니다."),
    LINE_FARE_GREATER_ZERO("노선 요금은 0 이상이어야 합니다."),
    NOT_EXIST_AGE_FARE_POLICY("연령별 요금 정책이 존재하지 않습니다.");

    private final String message;

    public String message() {
        return message;
    }

    ErrorEnum(String message) {
        this.message  = message;
    }
}
