package nextstep.subway.favorite.exception;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class NoFavoriteException extends BadRequestException {
    public NoFavoriteException(Long id) {
        super(noFavorite(id));
    }
}
