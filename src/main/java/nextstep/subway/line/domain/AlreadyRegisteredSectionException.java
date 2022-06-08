package nextstep.subway.line.domain;

public class AlreadyRegisteredSectionException extends RuntimeException {

    public AlreadyRegisteredSectionException() {
        super("이미 등록된 구간 입니다.");
    }
}
