package nextstep.subway.line.application.exception;

import nextstep.subway.common.NotFoundException;

public class LineNotFoundException extends NotFoundException {

    public LineNotFoundException() {
        super("지하철 노선을 찾을 수 없습니다.");
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
