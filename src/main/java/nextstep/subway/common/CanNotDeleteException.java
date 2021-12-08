package nextstep.subway.common;

import nextstep.subway.auth.application.AuthorizationException;

public class CanNotDeleteException extends AuthorizationException {
    public CanNotDeleteException() {
        super(Message.MESSAGE_CAN_NOT_DELETE.getMessage());
    }

    public CanNotDeleteException(String message) {
        super(message);
    }
}
