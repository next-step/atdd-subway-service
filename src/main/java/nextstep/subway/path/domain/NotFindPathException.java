package nextstep.subway.path.domain;

public class NotFindPathException extends RuntimeException {

    private static final long serialVersionUID = -1844613090795284240L;

    public NotFindPathException(String message) {
        super(message);
    }
}
