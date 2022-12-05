package nextstep.subway.fare.exception;

public class AgeFareException extends RuntimeException{
    public AgeFareException(AgeFareExceptionCode code){
        super(code.getMessage());
    }
}
