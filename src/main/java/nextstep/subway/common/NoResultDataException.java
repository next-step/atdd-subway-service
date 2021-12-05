package nextstep.subway.common;

import static nextstep.subway.common.Message.MESSAGE_NO_RESULT_DATA;

import javax.persistence.EntityNotFoundException;

public class NoResultDataException extends EntityNotFoundException {

    public NoResultDataException() {
        super(MESSAGE_NO_RESULT_DATA.getMessage());
    }

    public NoResultDataException(String message) {
        super(message);
    }
}
