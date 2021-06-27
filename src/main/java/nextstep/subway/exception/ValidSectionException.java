package nextstep.subway.exception;

import nextstep.subway.line.domain.Section;

public class ValidSectionException extends SubwayCommonException {

    public ValidSectionException() {
    }

    public ValidSectionException(String message) {
        super(message);
    }

    public ValidSectionException(String message, Section section) {
        super(String.format(message, section.getUpStation().getName(), section.getDownStation().getName()));
    }
}
