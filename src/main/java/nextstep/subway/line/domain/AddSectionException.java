package nextstep.subway.line.domain;

public class AddSectionException extends RuntimeException {

    private static final long serialVersionUID = 4264936228055568097L;

    public AddSectionException() {
    }

    public AddSectionException(String message) {
        super(message);
    }
}
