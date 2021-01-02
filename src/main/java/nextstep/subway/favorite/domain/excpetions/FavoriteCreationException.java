package nextstep.subway.favorite.domain.excpetions;

public class FavoriteCreationException extends RuntimeException {
    public FavoriteCreationException(final String message) {
        super(message);
    }
}
