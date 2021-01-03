package nextstep.subway.path.domain.exceptions;

public class CannotFindTransferFeeException extends RuntimeException {
    public CannotFindTransferFeeException(final String message) {
        super(message);
    }
}
