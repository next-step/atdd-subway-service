package nextstep.subway.member.exception;

public class NotFoundFavoriteException extends RuntimeException {

    public NotFoundFavoriteException() {
        super();
    }

    public NotFoundFavoriteException(String message) {
        super(message);
    }
}
