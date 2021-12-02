package nextstep.subway.exception;

public class NotFoundMemberException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "사용자를 찾을 수 없습니다.";

    public NotFoundMemberException() {
        super(EXCEPTION_MESSAGE);
    }
}
