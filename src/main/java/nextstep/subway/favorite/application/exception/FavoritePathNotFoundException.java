package nextstep.subway.favorite.application.exception;

import nextstep.subway.common.NotFoundException;

public class FavoritePathNotFoundException extends NotFoundException {
    public FavoritePathNotFoundException() {
        super("즐겨찾기 경로를 찾을 수 없습니다.");
    }

    public FavoritePathNotFoundException(String message) {
        super(message);
    }
}
