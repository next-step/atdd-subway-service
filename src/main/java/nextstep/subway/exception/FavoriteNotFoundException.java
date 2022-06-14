package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException() {
        super("존재하지 않는 즐겨찾기입니다.");
    }
}
