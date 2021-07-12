package nextstep.subway.line.exeption;

public class NotFoundSectionException extends RuntimeException {

    public static final String MESSAGE = "구간을 조회 할 수 없습니다.";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
