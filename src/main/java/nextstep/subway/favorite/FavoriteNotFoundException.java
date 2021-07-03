package nextstep.subway.favorite;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "favorite not found")
public class FavoriteNotFoundException extends RuntimeException {

    public FavoriteNotFoundException(long id) {
        this(String.format("%d의 식별자를 가진 Favorite는 존재하지 않습니다.", id));
    }

    public FavoriteNotFoundException(final String message) {
        super(message);
    }
}
