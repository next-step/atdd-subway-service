package nextstep.subway.advice.exception;

public class MemberBadRequestException extends RuntimeException {

    public MemberBadRequestException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
