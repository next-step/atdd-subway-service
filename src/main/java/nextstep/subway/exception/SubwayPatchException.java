package nextstep.subway.exception;

public class SubwayPatchException extends SubwayCommonException {

    public SubwayPatchException() {
    }

    public SubwayPatchException(String message) {
        super(message);
    }

    public SubwayPatchException(String message, Long id) {
        super(String.format(message, id));
    }

    public SubwayPatchException(String message, int age) {
        super(String.format(message, age));
    }
}
