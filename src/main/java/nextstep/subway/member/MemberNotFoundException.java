package nextstep.subway.member;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "member not found")
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(long id) {
        this(String.format("%d의 식별자를 가진 Member는 존재하지 않습니다.", id));
    }

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
