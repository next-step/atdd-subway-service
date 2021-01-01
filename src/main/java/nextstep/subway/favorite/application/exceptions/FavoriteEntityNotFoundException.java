package nextstep.subway.favorite.application.exceptions;

public class FavoriteEntityNotFoundException extends RuntimeException {
    public FavoriteEntityNotFoundException(final String message) {
        super(message);
    }
}
