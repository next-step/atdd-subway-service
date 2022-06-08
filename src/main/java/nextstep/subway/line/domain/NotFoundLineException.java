package nextstep.subway.line.domain;

public class NotFoundLineException extends RuntimeException {

    public NotFoundLineException(Long id) {
        super(String.format("라인이 존재하지 않습니다. (입력 값: %d)", id));
    }
}
