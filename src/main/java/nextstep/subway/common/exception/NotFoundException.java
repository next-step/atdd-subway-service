package nextstep.subway.common.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "해당 정보는 존재하지 않습니다.";

    public NotFoundException() {
        super(NOT_FOUND);
    }
}
