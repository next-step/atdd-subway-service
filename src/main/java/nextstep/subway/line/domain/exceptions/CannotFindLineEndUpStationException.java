package nextstep.subway.line.domain.exceptions;

public class CannotFindLineEndUpStationException extends RuntimeException {
    public CannotFindLineEndUpStationException(final String message) {
        super(message);
    }
}
