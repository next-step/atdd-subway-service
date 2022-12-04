package nextstep.subway.auth.exception;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException() {
        super("존재하지 않는 회원입니다.");
    }
}
