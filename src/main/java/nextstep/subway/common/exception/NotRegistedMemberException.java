package nextstep.subway.common.exception;

import java.util.NoSuchElementException;

public class NotRegistedMemberException extends NoSuchElementException {
    public NotRegistedMemberException() {
        super();
    }

    public NotRegistedMemberException(String errorMessage) {
        super(errorMessage);
    }
}