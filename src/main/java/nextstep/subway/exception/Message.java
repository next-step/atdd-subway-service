package nextstep.subway.exception;

public enum Message {

    ERROR_SECTION_ALREADY_EXISTS("이미 등록된 구간입니다"),
    ERROR_ONE_OF_STATION_SHOULD_BE_REGISTERED("모든 지하철역이 노선에 속하지 않아 등록할 수 없는 구간 입니다."),
    ERROR_SECTIONS_SIZE_TOO_SMALL_TO_DELETE("등록된 구간의 갯수가 적어 삭제할 수 없습니다."),
    ERROR_INPUT_DISTANCE_SHOULD_BE_LESS_THAN_EXISTING_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요."),
    ERROR_DISTANCE_TOO_SHORT_TO_BE_CREATED("구간의 거리가 너무 짧습니다"),
    ERROR_CANNOT_FIND_LINE("등록되지 않은 노선입니다."),
    ERROR_CANNOT_FIND_MEMBER("등록되지 않은 사용자입니다."),
    ERROR_CANNOT_FIND_STATION("등록되지 않은 지하철역입니다."),
    ERROR_START_AND_END_STATIONS_ARE_SAME("출발역과 도착역이 동일합니다. 다른 역을 입력해주세요."),
    ERROR_START_OR_END_STATIONS_NOT_REGISTERED("출발역이나 도착역이 등록되어 있지 않습니다."),
    ERROR_PATH_NOT_FOUND("경로를 조회할 수 없습니다."),
    ERROR_MEMBER_NOT_REGISTERED("등록되지 않은 사용자입니다."),
    ERROR_WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),
    ERROR_CANNOT_FIND_FAVORITE("즐겨찾기를 찾을 수 없습니다.");

    private String text;

    Message(String text) {
        this.text = text;
    }

    public String showText() {
        return text;
    }
}
