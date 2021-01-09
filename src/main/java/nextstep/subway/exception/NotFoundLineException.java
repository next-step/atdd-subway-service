package nextstep.subway.exception;

public class NotFoundLineException extends BadRequestException {
    public NotFoundLineException() {
        super("노선 정보를 찾을 수 없습니다.");
    }
}
