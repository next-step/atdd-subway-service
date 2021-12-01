package nextstep.subway.exception;

public class SectionDistanceOverException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public SectionDistanceOverException() {
        super(EXCEPTION_MESSAGE);
    }
}
