package nextstep.subway.favorite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFavoriteException extends NoSuchElementException {
    public static final String NOT_FOUND_ELEMENT = "해당하는 즐겨찾기를 찾을 수 없습니다.";

    public NotFoundFavoriteException() {
        super(NOT_FOUND_ELEMENT);
    }
}
