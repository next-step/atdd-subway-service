package nextstep.subway.common.Excetion;

public class UnableTokenException extends RuntimeException{
    public UnableTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
