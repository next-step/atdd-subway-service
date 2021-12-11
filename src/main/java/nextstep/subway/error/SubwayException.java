package nextstep.subway.error;

public class SubwayException extends RuntimeException {

    protected SubwayException() {
    }

    public SubwayException(String message) {
        super(message);
    }
}
