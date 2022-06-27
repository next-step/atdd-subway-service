package nextstep.subway.favorite.exception;

public class FavoriteException extends RuntimeException {
    public FavoriteException(final FavoriteExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public FavoriteException(final String message) {
        super(message);
    }
}
