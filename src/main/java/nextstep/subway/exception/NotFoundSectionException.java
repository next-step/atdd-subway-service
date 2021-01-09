package nextstep.subway.exception;

public class NotFoundSectionException extends BadRequestException {
    public NotFoundSectionException() {
        super("구간 정보를 찾을 수 없습니다.");
    }

    public NotFoundSectionException(String message) {
        super(message);
    }
}
