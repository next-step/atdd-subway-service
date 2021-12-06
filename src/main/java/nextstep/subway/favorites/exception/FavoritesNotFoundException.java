package nextstep.subway.favorites.exception;

import nextstep.subway.common.exception.NotFoundException;

public class FavoritesNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "해당 즐겨찾기를 찾을 수 없습니다. : ";
    public FavoritesNotFoundException(Long id) {
        super(DEFAULT_MESSAGE + id);
    }
}
