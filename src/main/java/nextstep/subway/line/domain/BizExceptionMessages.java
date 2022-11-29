package nextstep.subway.line.domain;

public enum BizExceptionMessages {
    LINE_NAME_INVALID("노선의 이름은 공백일 수 없습니다"),
    LINE_COLOR_INVALID("노선의 색은 공백일 수 없습니다"),
    LINE_MIN_SECTIONS_SIZE("한 노선에는 최소 1개의 구간이 필요합니다."),
    SECTION_ALREADY_REGISTERED("이미 등록된 구간 입니다."),
    SECTION_NOT_REACHABLE_ANY_STATION("등록할 수 없는 구간 입니다."),
    SECTION_UNENROLLABLE_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");

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
