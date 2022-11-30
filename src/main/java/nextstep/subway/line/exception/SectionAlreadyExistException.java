package nextstep.subway.line.exception;

public class SectionAlreadyExistException extends RuntimeException {

    public SectionAlreadyExistException() {
        super("이미 등록된 구간 입니다.");
    }
}
