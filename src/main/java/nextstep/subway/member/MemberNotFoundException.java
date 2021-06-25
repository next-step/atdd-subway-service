package nextstep.subway.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "member not found")
public class MemberNotFoundException extends RuntimeException {
}
