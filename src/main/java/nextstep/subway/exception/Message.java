package nextstep.subway.exception;

public enum Message {

    ERROR_SECTION_ALREADY_EXISTS("이미 등록된 구간입니다"),
    ERROR_ONE_OF_STATION_SHOULD_BE_REGISTERED("모든 지하철역이 노선에 속하지 않아 등록할 수 없는 구간 입니다."),
    ERROR_SECTIONS_SIZE_TOO_SMALL_TO_DELETE("등록된 구간의 갯수가 적어 삭제할 수 없습니다."),
    ERROR_INPUT_DISTANCE_SHOULD_BE_LESS_THAN_EXISTING_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");

    private String text;

    Message(String text) {
        this.text = text;
    }

    public String showText() {
        return text;
    }
}
