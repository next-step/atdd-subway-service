package nextstep.subway.favorite.application.exceptions;

public class NotMyFavoriteException extends RuntimeException {
    public NotMyFavoriteException(final String message) {
        super(message);
    }
}
