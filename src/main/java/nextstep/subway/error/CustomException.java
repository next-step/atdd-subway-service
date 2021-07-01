package nextstep.subway.error;

public class CustomException extends RuntimeException{
    public CustomException(ErrorMessage errorMessage) {
        super(errorMessage.toString());
    }
}
