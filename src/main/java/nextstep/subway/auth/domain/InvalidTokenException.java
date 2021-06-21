package nextstep.subway.auth.domain;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = -5478954042778595603L;

    public InvalidTokenException() {
        super("계정 인증이 만료되었거나 정확하지 않습니다. 재로그인 후 시도해주세요.");
    }
}
