package nextstep.subway.error;

public class SubwayBadRequestException extends RuntimeException {

    protected SubwayBadRequestException() {
    }

    public SubwayBadRequestException(String message) {
        super(message);
    }
}
