package nextstep.subway.member.application.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "요청한 정보에 해당하는 사용자를 찾을 수 없습니다.";

    public MemberNotFoundException(){
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
