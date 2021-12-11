package nextstep.subway.error;

public class SubwayException extends IllegalArgumentException {

    protected SubwayException() {
    }

    public SubwayException(String message) {
        super(message);
    }
}
