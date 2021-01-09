package nextstep.subway.exception;

public class NotFoundMemberException extends BadRequestException {
    public NotFoundMemberException() {
        super("멤버 정보를 찾을 수 없습니다.");
    }
}
