package nextstep.subway.favorite.application;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FavoriteNotFoundException extends SubwayException {
    public FavoriteNotFoundException(Object arg) {
        super(arg);
    }
}
