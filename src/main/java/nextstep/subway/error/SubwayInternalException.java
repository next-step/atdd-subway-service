package nextstep.subway.error;

public class SubwayInternalException extends RuntimeException {

    protected SubwayInternalException() {
    }

    public SubwayInternalException(String message) {
        super(message);
    }
}
