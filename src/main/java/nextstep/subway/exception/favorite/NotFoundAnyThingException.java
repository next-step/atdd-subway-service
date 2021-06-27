package nextstep.subway.exception.favorite;

public class NotFoundAnyThingException extends RuntimeException {

    private static final long serialVersionUID = 6187640947702177868L;
    private static final String NO_FOUND = "유저, 혹은 역들을 확인하세요";

    public NotFoundAnyThingException() {
        super(NO_FOUND);
    }

    public NotFoundAnyThingException(String msg) {
        super(msg);
    }
}
