package nextstep.subway.exception;

import java.util.NoSuchElementException;

public class NoSearchMemberException extends NoSuchElementException {
    private static final String MESSAGE = "해당하는 사용자를 찾을 수 없습니다. (%d)";

    public NoSearchMemberException(String email) {
        super(String.format(MESSAGE, email));
    }
}
