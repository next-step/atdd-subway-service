package nextstep.subway.exception;

public class NotFoundStationException extends BadRequestException {
    public NotFoundStationException() {
        super("역 정보를 찾을 수 없습니다.");
    }

    public NotFoundStationException(String message) {
        super(message);
    }
}
