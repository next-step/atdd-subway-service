package nextstep.subway.line.application.exception;

import nextstep.subway.common.NotFoundException;

public class SectionNotFoundException extends NotFoundException {

    public static final SectionNotFoundException NOT_FOUND_TERMINUS = new SectionNotFoundException("상행 종점역을 찾을 수 없습니다.");
    public static final SectionNotFoundException BREAK_SECTION = new SectionNotFoundException("이어지는 구간을 찾을 수 없습니다.");

    public SectionNotFoundException() {
    }

    public SectionNotFoundException(String message) {
        super(message);
    }
}
