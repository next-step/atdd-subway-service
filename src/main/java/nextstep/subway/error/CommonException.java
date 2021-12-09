package nextstep.subway.error;

public class CommonException extends IllegalArgumentException {

    protected CommonException() {
    }

    public CommonException(String message) {
        super(message);
    }
}
