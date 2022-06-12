package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionAddException extends RuntimeException {
    public static final String SECTION_HAS_UP_AND_DOWN_STATION_MSG = "추가하려는 구간이 기존에 등록되어 있는 구간입니다.";
    public static final String SECTION_HAS_NOT_UP_AND_DOWN_STATION_MSG = "추가하려는 구간은 기존 구간의 상행과 하행 중 하나를 포함해야 합니다.";

    public SectionAddException(String msg) {
        super(msg);
    }
}
