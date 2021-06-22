package nextstep.subway.exception;

public class SectionNotConnectedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "구간이 연결되어 있지 않습니다.";

    public SectionNotConnectedException() {
        super(DEFAULT_MESSAGE);
    }

    public SectionNotConnectedException(String message) {
        super(message);
    }
}
