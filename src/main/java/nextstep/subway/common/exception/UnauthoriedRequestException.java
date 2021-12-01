package nextstep.subway.common.exception;

public class UnauthoriedRequestException extends IllegalAccessError {
 
    public UnauthoriedRequestException() {
        super();
    }

    public UnauthoriedRequestException(String errorMessage) {
        super(errorMessage);
    }
}
