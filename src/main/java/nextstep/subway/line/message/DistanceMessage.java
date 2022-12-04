package nextstep.subway.line.message;

public enum DistanceMessage {
    CREATE_ERROR_MORE_THAN_ONE("거리는 1이상이어야 합니다.");

    private final String message;

    DistanceMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
