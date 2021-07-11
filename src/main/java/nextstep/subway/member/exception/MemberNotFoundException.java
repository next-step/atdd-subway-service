package nextstep.subway.member.exception;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(Long id) {
        super(format("%d인 회원을 찾을 수 없습니다.", id));
    }
}
