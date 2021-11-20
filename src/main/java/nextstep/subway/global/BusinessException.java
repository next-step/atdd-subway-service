package nextstep.subway.global;

public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }
}
