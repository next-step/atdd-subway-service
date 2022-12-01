package nextstep.subway.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFavoriteException extends RuntimeException {

    public NotFoundFavoriteException(Long favoriteId) {
        super(format("즐겨찾기를 찾을 수 없습니다, %s", favoriteId));
    }
}
