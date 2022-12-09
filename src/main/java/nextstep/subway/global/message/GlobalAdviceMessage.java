package nextstep.subway.global.message;

public enum GlobalAdviceMessage {
    NOT_FOUND_ENTITY("해당 도메인을 조회할 수 없습니다");

    private final String message;

    GlobalAdviceMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
