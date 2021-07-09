package nextstep.subway.member.exception;

public class NotFoundMemberException extends RuntimeException{
    public static final String MESSAGE = "회원을 조회 할 수 없습니다.";

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
