package nextstep.subway.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String format, Long id) {
        super(String.format(format, id));
    }
}
