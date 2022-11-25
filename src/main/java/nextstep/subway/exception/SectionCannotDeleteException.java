package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionCannotDeleteException extends RuntimeException {
    public SectionCannotDeleteException() {
        super("단일 구간 노선은 삭제할 수 없습니다.");
    }
}
