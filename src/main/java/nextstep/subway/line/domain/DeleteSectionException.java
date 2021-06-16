package nextstep.subway.line.domain;

public class DeleteSectionException extends RuntimeException {

    private static final long serialVersionUID = 1550107128073413229L;

    public DeleteSectionException() {
    }

    public DeleteSectionException(String message) {
        super(message);
    }
}
