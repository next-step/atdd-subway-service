package nextstep.subway.exception;

public class NotFoundException extends BusinessException{

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
