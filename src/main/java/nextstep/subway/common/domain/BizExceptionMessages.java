package nextstep.subway.common.domain;

public enum BizExceptionMessages {
    LINE_NAME_INVALID("노선의 이름은 공백일 수 없습니다"),
    LINE_COLOR_INVALID("노선의 색은 공백일 수 없습니다"),
    LINE_MIN_SECTIONS_SIZE("한 노선에는 최소 1개의 구간이 필요합니다."),
    SECTION_ALREADY_REGISTERED("이미 등록된 구간 입니다."),
    SECTION_NOT_REACHABLE_ANY_STATION("등록할 수 없는 구간 입니다."),
    SECTION_IS_NOT_CONTAIN_STATION("구간에 속하지 않은 역입니다."),
    SECTION_UNENROLLABLE_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    PATH_SAME_SOURCE_WITH_TARGET("출발역과 도착역이 같습니다. 입력값을 다시 확인해주세요."),
    PATH_UNNORMAL_STATION("유효하지 않은 역입니다. 입력값을 다시 확인해주세요."),
    AUTHORIZATION_WRONG_ACCESS_TOKEN("AccessToken을 다시 확인 해주세요."),
    FAVORITE_IS_DUPLICATION("이미 존재하는 즐겨찾기 입니다."),
    FAVORITE_IS_NOT_REMOVABLE("현재 유저의 즐겨찾기에 존재하지 않습니다.");

    private final String message;

    BizExceptionMessages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "BizExceptionMessages{" +
                "message='" + message + '\'' +
                '}';
    }
}
