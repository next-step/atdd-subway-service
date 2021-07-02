package nextstep.subway.station;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "station not found")
public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(long id) {
        this(String.format("%d의 식별자를 가진 Station는 존재하지 않습니다.", id));
    }

    public StationNotFoundException(final String message) {
        super(message);
    }
}
