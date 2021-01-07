package nextstep.subway.line.domain;

public class CannotRemoveSectionException extends RuntimeException {
    public CannotRemoveSectionException(String message) {
        super(message);
    }
}
