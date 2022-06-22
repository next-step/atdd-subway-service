package nextstep.subway.exception;

import java.util.function.Supplier;

public class AddLineSectionFailException extends RuntimeException  {
    public AddLineSectionFailException(String message) {
        super(message);
    }
}
