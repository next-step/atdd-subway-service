package nextstep.subway.favorite.application;

public class NoSuchFavoriteException extends RuntimeException {
    public NoSuchFavoriteException(String msg) {
        super(msg);
    }
}
