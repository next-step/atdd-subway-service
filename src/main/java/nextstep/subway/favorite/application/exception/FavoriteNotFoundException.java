package nextstep.subway.favorite.application.exception;

import nextstep.subway.common.NotFoundException;

public class FavoriteNotFoundException extends NotFoundException {
    public FavoriteNotFoundException() {
        super("즐겨찾기 경로를 찾을 수 없습니다.");
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
