package nextstep.subway.line.domain;

public class CannotRegisterSectionException extends RuntimeException {

    public CannotRegisterSectionException() {
        super("등록할 수 없는 구간 입니다.");
    }
}
