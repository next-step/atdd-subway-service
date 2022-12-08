package nextstep.subway.exception;

public class NotFoundOverFarePolicyException extends BadRequestException {
    public NotFoundOverFarePolicyException(String message) {
        super(message);
    }
}
