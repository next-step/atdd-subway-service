package nextstep.subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "section not found")
public class SectionNotFoundException extends RuntimeException {
}
