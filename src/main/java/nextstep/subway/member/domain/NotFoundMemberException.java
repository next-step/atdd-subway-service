package nextstep.subway.member.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
