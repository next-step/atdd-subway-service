package nextstep.subway.line.message;

public enum SectionMessage {
    ADD_ERROR_NONE_MATCH_SECTION_STATIONS("등록할 수 없는 구간 입니다."),
    ADD_ERROR_ALREADY_ENROLLED_STATIONS("이미 등록된 구간 입니다."),
    REMOVE_ERROR_MORE_THAN_TWO_SECTIONS("구간이 2개 이상이어야 역을 제거 할 수 있습니다."),
    MERGE_ERROR_NOT_EQUALS_LINE("지하철 노선이 다른 역은 합칠 수 없습니다.");

    private final String message;

    SectionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
