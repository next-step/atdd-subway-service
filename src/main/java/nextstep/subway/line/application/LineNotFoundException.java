package nextstep.subway.line.application;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "line not found")
public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException(long id) {
        this(String.format("%d의 식별자를 가진 Line은 존재하지 않습니다.", id));
    }

    public LineNotFoundException(final String message) {
        super(message);
    }
}
