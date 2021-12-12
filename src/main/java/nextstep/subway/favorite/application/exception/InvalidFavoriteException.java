package nextstep.subway.favorite.application.exception;

import nextstep.subway.common.InvalidException;

public class InvalidFavoriteException extends InvalidException {

    public InvalidFavoriteException() {
        super("출발역과 도착역이 같습니다.");
    }

    public InvalidFavoriteException(String message) {
        super(message);
    }
}
