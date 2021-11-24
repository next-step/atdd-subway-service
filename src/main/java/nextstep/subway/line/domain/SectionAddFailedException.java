package nextstep.subway.line.domain;

import nextstep.subway.global.domain.BusinessException;

public class SectionAddFailedException extends BusinessException {

    public SectionAddFailedException() {
        super();
    }

    public SectionAddFailedException(String message) {
        super(message);
    }
}
