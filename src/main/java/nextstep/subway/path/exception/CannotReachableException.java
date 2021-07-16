package nextstep.subway.path.exception;

import static java.lang.String.format;

public class CannotReachableException extends IllegalStateException {
    public CannotReachableException() {
    }

    public CannotReachableException(String source, String target) {
        super(format("%s와(과) %s이(가) 이어져 있지 않습니다."
                , source
                , target));
    }
}
