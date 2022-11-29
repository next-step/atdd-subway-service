package nextstep.subway.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_ERROR = "데이터가 존재하지 않습니다.";

    public NotFoundException() {
        super(NOT_FOUND_ERROR);
    }
}
