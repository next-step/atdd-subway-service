package nextstep.subway.error;

public enum ErrorCode {
    EXISTS_BOTH_STATIONS("이미 등록된 구간 입니다."),
    NO_EXISTS_BOTH_STATIONS("등록할 수 없는 구간 입니다."),
    TOO_LONG_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");

    ErrorCode(String message) {
        this.message = message;
    }

    String message;
}
