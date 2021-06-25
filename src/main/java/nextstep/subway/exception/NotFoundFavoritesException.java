package nextstep.subway.exception;

public class NotFoundFavoritesException extends RuntimeException {
    public NotFoundFavoritesException() {
    }

    public NotFoundFavoritesException(String message) {
        super(message);
    }
}
