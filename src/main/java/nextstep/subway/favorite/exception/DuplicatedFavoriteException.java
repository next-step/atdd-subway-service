package nextstep.subway.favorite.exception;

public class DuplicatedFavoriteException extends IllegalFavoriteArgumentException {
    private static final String DEFAULT_MESSAGE = "이미 등록된 즐겨찾기 입니다.";

    public DuplicatedFavoriteException() {
        super(DEFAULT_MESSAGE);
    }
}
