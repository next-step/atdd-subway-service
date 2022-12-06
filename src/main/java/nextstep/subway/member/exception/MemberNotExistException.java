package nextstep.subway.member.exception;

import java.util.NoSuchElementException;

public class MemberNotExistException extends NoSuchElementException {

    public MemberNotExistException(String message) {
        super(message);
    }
}
