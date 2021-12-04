package nextstep.subway.exception;

public class IsEqualsTwoStationsException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "출발역과 도착역이 같습니다.";

    public IsEqualsTwoStationsException() {
        super(EXCEPTION_MESSAGE);
    }
}
