package nextstep.subway.line;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "section not found")
public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException(long id) {
        this(String.format("%d의 식별자를 가진 Section은 존재하지 않습니다.", id));
    }

    public SectionNotFoundException(final String message) {
        super(message);
    }
}
