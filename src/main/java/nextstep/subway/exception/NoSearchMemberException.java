package nextstep.subway.exception;

import java.util.NoSuchElementException;

public class NoSearchMemberException extends NoSuchElementException {
    private static final String MESSAGE_ID = "해당하는 사용자를 찾을 수 없습니다. (id:%d)";
    private static final String MESSAGE_EMAIL = "해당하는 사용자를 찾을 수 없습니다. (email:%d)";

    public NoSearchMemberException(Long id) {
        super(String.format(MESSAGE_ID, id));
    }

    public NoSearchMemberException(String email) {
        super(String.format(MESSAGE_EMAIL, email));
    }
}
