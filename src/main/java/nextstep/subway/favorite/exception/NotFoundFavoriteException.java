package nextstep.subway.favorite.exception;

public class NotFoundFavoriteException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "해당 유저의 즐겨찾기가 존재하지 않습니다.";

    public NotFoundFavoriteException() {
        super(DEFAULT_MESSAGE);
    }
}
