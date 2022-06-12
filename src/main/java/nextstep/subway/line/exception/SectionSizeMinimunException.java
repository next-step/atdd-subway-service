package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionSizeMinimunException extends RuntimeException {
    public static final String SECTION_SIZE_MINIMUN_MSG = "구간내에 최소한 구간이 하나는 존재해야 합니다.";

    public SectionSizeMinimunException() {
        super(SECTION_SIZE_MINIMUN_MSG);
    }
}
