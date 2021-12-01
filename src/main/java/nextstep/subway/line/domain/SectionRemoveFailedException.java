package nextstep.subway.line.domain;

import nextstep.subway.common.domain.BusinessException;

public class SectionRemoveFailedException extends BusinessException {

    public SectionRemoveFailedException(String message) {
        super(message);
    }
}
