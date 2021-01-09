package nextstep.subway.exception;

public class SubwayException extends RuntimeException {

    public SubwayException() {
        super();
    }

    public SubwayException(Object arg) {
        super(String.valueOf(arg));
    }
}
