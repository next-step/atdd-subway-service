package nextstep.subway.common.exception;

public enum SubwayErrorCode {
    NOT_LOWER_THAN_ORIGINAL_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    INVALID_DISTANCE("1 이상의 길이만 입력 가능합니다.");

    private final String message;

    SubwayErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

