package nextstep.subway.exception.domain;

public class SubwayException extends RuntimeException {
    public SubwayException(final SubwayExceptionMessage message) {
        super(message.getMessage());
    }
}
