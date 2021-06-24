package nextstep.subway.exception;

public class CannotDeleteException extends RuntimeException {

    public CannotDeleteException(Message message) {
        super(message.showText());
    }
}
