package nextstep.subway.favorite.application;

import nextstep.subway.common.error.ErrorCode;
import nextstep.subway.common.error.exception.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {
    public FavoriteNotFoundException() {
        super(ErrorCode.FAVORITE_NOT_FOUND);
    }
}
