package nextstep.subway.path.domain;

public class NotFoundPathException extends RuntimeException {

    private static final long serialVersionUID = -1844613090795284240L;

    public NotFoundPathException(String message) {
        super(message);
    }
}
