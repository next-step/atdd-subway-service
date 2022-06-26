package nextstep.subway.exception;

import java.util.NoSuchElementException;

public class NoSearchFavoriteException extends NoSuchElementException {
    private static final String MESSAGE = "해당하는 즐겨찾기를 찾을 수 없습니다. (id: %d)";

    public NoSearchFavoriteException(Long favoriteId) {
        super(String.format(MESSAGE, favoriteId));
    }
}
