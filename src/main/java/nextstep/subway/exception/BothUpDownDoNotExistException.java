package nextstep.subway.exception;

public class BothUpDownDoNotExistException extends IllegalArgumentException {
    public BothUpDownDoNotExistException(String message) {
        super(message);
    }
}
