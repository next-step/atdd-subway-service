package nextstep.subway.exception;

import java.util.function.Supplier;

public class AddLineSectionFail extends RuntimeException implements Supplier<AddLineSectionFail> {
    public AddLineSectionFail(String message) {
        super(message);
    }

    @Override
    public AddLineSectionFail get() {
        return this;
    }
}
