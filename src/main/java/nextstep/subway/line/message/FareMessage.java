package nextstep.subway.line.message;

public enum FareMessage {
    FARE_SHOULD_BE_MORE_THAN_ZERO("요금은 0원 이상이어야 합니다.");

    private final String message;

    FareMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
