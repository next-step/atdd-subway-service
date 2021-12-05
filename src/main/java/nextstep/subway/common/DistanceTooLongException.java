package nextstep.subway.common;

public class DistanceTooLongException extends IllegalArgumentException {

    public DistanceTooLongException(final String message) {
        super(message);
    }
}
