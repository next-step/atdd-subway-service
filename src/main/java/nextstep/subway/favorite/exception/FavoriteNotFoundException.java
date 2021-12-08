package nextstep.subway.favorite.exception;

import nextstep.subway.common.NoResultDataException;

public class FavoriteNotFoundException extends NoResultDataException {

    public FavoriteNotFoundException() {
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
