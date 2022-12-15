package nextstep.subway.fare.message;

public enum FareMessage {
    FARE_SHOULD_BE_MORE_THAN_ZERO("요금은 0원 이상이어야 합니다."),
    PATH_SHOULD_BE_NOT_NULL("거리 기준은 필수입니다."),
    MEMBER_SHOULD_BE_NOT_NULL("회원 정보는 필수입니다."),
    MEMBER_AGE_SHOULD_BE_NOT_NULL("회원 나이는 필수입니다."),
    STATIONS_SHOULD_BE_NOT_NULL("요금을 측정할 구간은 필수입니다."),
    ;

    private final String message;

    FareMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
