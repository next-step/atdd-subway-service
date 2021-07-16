package nextstep.subway.favorite.exception;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

public class FavoriteNotFoundException extends EntityNotFoundException {

    public FavoriteNotFoundException(Long id) {
        super(format("%d인 즐겨찾기를 찾을 수 없습니다.", id));
    }
}
