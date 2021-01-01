package nextstep.subway.favorite.domain.excpetions;

public class SafeStationInFavoriteException extends RuntimeException {
    public SafeStationInFavoriteException(final String message) {
        super(message);
    }
}
