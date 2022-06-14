package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }
}
