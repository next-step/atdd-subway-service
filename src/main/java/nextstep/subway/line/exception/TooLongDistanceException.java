package nextstep.subway.line.exception;

public class TooLongDistanceException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public TooLongDistanceException() {
        super(DEFAULT_MESSAGE);
    }
}
