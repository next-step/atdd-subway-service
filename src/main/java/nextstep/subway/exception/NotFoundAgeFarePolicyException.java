package nextstep.subway.exception;

public class NotFoundAgeFarePolicyException extends BadRequestException {
    public NotFoundAgeFarePolicyException(String message) {
        super(message);
    }
}
