package nextstep.subway.favorite.application.exceptions;

import nextstep.subway.exceptions.EntityNotFoundException;

public class FavoriteEntityNotFoundException extends EntityNotFoundException {
    public FavoriteEntityNotFoundException(final String message) {
        super(message);
    }
}
